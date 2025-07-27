package org.mattshoe.shoebox.listery.ai.parseweb.usecase

import android.webkit.URLUtil
import crawlercommons.robots.SimpleRobotRulesParser
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsText
import org.mattshoe.shoebox.listery.ai.GenerativeChat
import org.mattshoe.shoebox.listery.ai.GenerativeError
import org.mattshoe.shoebox.listery.ai.GenerativeResult
import org.mattshoe.shoebox.listery.ai.common.RecipeJsonSchema
import org.mattshoe.shoebox.listery.config.RemoteConfigRepository
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.model.Recipe
import org.mattshoe.shoebox.listery.util.RetryResult
import org.mattshoe.shoebox.listery.util.retry
import java.net.URL
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class ParseWebsiteUseCase @Inject constructor(
    private val generativeChat: GenerativeChat,
    private val httpClient: HttpClient,
    private val remoteConfig: RemoteConfigRepository
) {
    private var blacklist: DomainBlacklist? = null
    private val instructions = """
            You are tasked with extracting recipe information from a website and converting it into a specific JSON format.
            
            Notes:
            1. Extract the recipe name, ingredients, servings count, and cooking steps from the website content
            2. For ingredients, estimate calories if not provided (use reasonable defaults)
            3. Convert prep time to seconds
            4. If any information is missing, use null or empty arrays as appropriate
        """.trimIndent()

    init {
        blacklist = remoteConfig.get<DomainBlacklist>("DOMAIN_BLACKLIST")
    }

    suspend fun execute(site: String): GenerativeResult<Recipe> {
        val url = site.toJavaUrl()

        blacklist?.entry(site)?.let {
            return GenerativeResult.Failure(
                reason = GenerativeError(it.reason)
            )
        }

        if (!url.isUrlAllowedToBeScraped()) {
            return GenerativeResult.Failure(
                reason = GenerativeError(
                    explanation = "${url.host} does not allow scraping!"
                )
            )
        }

        val websiteContent = fetchWebsiteContent(url)
        val prompt = createRecipeExtractionPrompt(websiteContent, url)

        return generativeChat.execute<Recipe>(
            instructions,
            RecipeJsonSchema,
            prompt
        )
    }

    private suspend fun fetchWebsiteContent(url: URL): String {
        val result = retry(delay = 2.seconds) {
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

    private fun createRecipeExtractionPrompt(websiteContent: String, originalUrl: URL): String {
        return """
            Website URL: $originalUrl
            Website Content:
            $websiteContent
        """.trimIndent()
    }

    suspend fun URL.isUrlAllowedToBeScraped(): Boolean {
        try {
            val robotsUrl = "${protocol}://${host}/robots.txt"

            // Fetch robots.txt
            val robotsContent = httpClient.get(robotsUrl)

            // Parse it
            val parser = SimpleRobotRulesParser()
            val rules = parser.parseContent(host, robotsContent.bodyAsBytes(), "text/plain", "ListeryBot")

            // Check if allowed
            return rules.isAllowed(toString())
        } catch (e: Exception) {
            loge(e)
            return true
        }
    }
}

fun String.toJavaUrl() = URL(
    if (URLUtil.isValidUrl(this)) this
    else "https://$this"
)
