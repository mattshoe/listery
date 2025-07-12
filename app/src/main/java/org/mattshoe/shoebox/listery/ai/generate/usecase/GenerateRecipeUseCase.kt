package org.mattshoe.shoebox.listery.ai.generate.usecase

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
import org.mattshoe.shoebox.listery.cuisine.model.Cuisine
import org.mattshoe.shoebox.listery.lifestyle.model.Lifestyle
import org.mattshoe.shoebox.listery.util.RetryResult
import org.mattshoe.shoebox.listery.util.retry

data class GenerateRecipeRequest(
    val cuisine: Cuisine?,
    val lifestyle: Lifestyle?,
    val prepTime: String?
)

class GenerateRecipeUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {
    private val httpClient = HttpClient(OkHttp)

    suspend fun execute(request: GenerateRecipeRequest): Recipe {
        val openai = OpenAI(
            token = remoteConfigRepository.getString("OPENAI_KEY"),
            timeout = Timeout(socket = 60.seconds),
        )
        
        // Create a prompt that will extract recipe information
        val prompt = createPrompt(request)
        
        // Use OpenAI to parse the recipe
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("o4-mini-2025-04-16"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = """
                        You are a chef of the most creative and sensible kind. 
                        You will receive requests for unique recipes with various constraints.
                        You will accept these requests and generate recipes according to the constraints.
                        However, your audience must be presumed to be regular people of low cooking skill.
                        Thus, your recipes should reflect that in their simplicity and language.
                        
                        But above ALL ELSE:
                        The recipe you generate MUST adhere perfectly to the following JSON schema:
                        
                        $RecipeJsonSchema
                        
                        Important, Unbreakable Rules:
                        1. Obey the JSON schema.
                        2. Ensure the JSON is valid.
                        3. Follow all constraints specified in the prompt.
                        4. Assume low-skill audience.
                        5. Your response must contain ONLY the plain json text. NO OTHER TEXT OR MARKDOWN OR FORMATTING.
                        6. I REPEAT: ONLY PLAIN TEXT JSON IN YOUR RESPONSE. NO OTHER MARKDOWN OR OTHER FORMATTING.
                        """.trimIndent()
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
                ?: throw Exception("Failed to generate recipe")

            // Deserialize the JSON response into a Recipe object
            Json.decodeFromString<Recipe>(jsonResponse)
        }

        return when (retryResult) {
            is RetryResult.Success -> retryResult.value
            is RetryResult.Failure -> throw retryResult.error
        }
    }


    private fun createPrompt(request: GenerateRecipeRequest): String {
        return """
                Cuisine: ${request.cuisine ?: "Any" }
                Lifestyle: ${request.lifestyle ?: "Any" }
                Prep Time: ${request.prepTime ?: "Any" }
            """.trimIndent()
    }
}
