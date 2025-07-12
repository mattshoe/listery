package org.mattshoe.shoebox.listery.ai.parseweb.usecase

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.mattshoe.shoebox.listery.config.RemoteConfigRepository
import org.mattshoe.shoebox.listery.model.Recipe
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.json.Json
import org.mattshoe.shoebox.listery.ai.common.RecipeJsonSchema
import org.mattshoe.shoebox.listery.util.RetryResult
import org.mattshoe.shoebox.listery.util.retry

class ParseWebsiteUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {
    private val httpClient = HttpClient(OkHttp)

    suspend fun execute(site: String): Recipe {
        val openai = OpenAI(
            token = remoteConfigRepository.getString("OPENAI_KEY"),
            timeout = Timeout(socket = 60.seconds),
        )

        // First, fetch the website content
        val websiteContent = fetchWebsiteContent(site)
        
        // Create a prompt that will extract recipe information
        val prompt = createRecipeExtractionPrompt(websiteContent, site)
        
        // Use OpenAI to parse the recipe
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("o4-mini-2025-04-16"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = "You are a recipe parsing assistant. Extract recipe information from website content and return it as valid JSON that matches the specified Recipe schema."
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = prompt
                )
            ),
//            temperature = 0.1, // Low temperature for consistent JSON output
        )

        val retryResult = retry {
            val response = openai.chatCompletion(chatCompletionRequest)
            val jsonResponse = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("Failed to parse recipe")

            // Deserialize the JSON response into a Recipe object
            Json.decodeFromString<Recipe>(jsonResponse)
        }

        return when (retryResult) {
            is RetryResult.Success -> retryResult.value
            is RetryResult.Failure -> throw retryResult.error
        }
    }

    private suspend fun fetchWebsiteContent(url: String): String {
        val result = retry {
            val textContent = httpClient
                .get(url)
                .bodyAsText()
                .sanitizeHtml()
            textContent.ifEmpty { "No text content found on the page" }
        }
        return when (result) {
            is RetryResult.Success -> result.value
            is RetryResult.Failure -> "Error fetching website content: ${result.error.message}"
        }
    }

    private fun String.sanitizeHtml(): String {
        // Basic HTML tag removal - in production, use a proper HTML parser
        return this
            .replace(Regex("<script[^>]*>.*?</script>", RegexOption.DOT_MATCHES_ALL), "") // Remove scripts
            .replace(Regex("<style[^>]*>.*?</style>", RegexOption.DOT_MATCHES_ALL), "") // Remove styles
            .replace(Regex("<[^>]*>"), "") // Remove all HTML tags
            .replace(Regex("\\s+"), " ") // Replace multiple whitespace with single space
            .trim()
    }

    private fun createRecipeExtractionPrompt(websiteContent: String, originalUrl: String): String {
        return """
You are tasked with extracting recipe information from a website and converting it into a specific JSON format.

Website URL: $originalUrl
Website Content:
$websiteContent

Please extract the recipe information from the website and return it as a valid JSON object that matches this exact schema:

${RecipeJsonSchema}

Important guidelines:
1. Extract the recipe name, ingredients, servings count, and cooking steps from the website content
2. For ingredients, estimate calories if not provided (use reasonable defaults)
3. Convert prep time to ISO 8601 duration format (PT30M, PT1H, PT1H30M, etc.)
4. If any information is missing, use null or empty arrays as appropriate
5. Ensure the JSON is valid and properly formatted
6. ONLY return the JSON object, no additional text or explanations
7. DO NOT SURROUND THE JSON WITH MARKDOWN. ONLY PLAIN TEXT.

Return ONLY the JSON object, no other text:
"""
    }
}
