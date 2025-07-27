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
import org.mattshoe.shoebox.listery.ai.GenerativeChat
import org.mattshoe.shoebox.listery.ai.GenerativeResult
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
    private val generativeChat: GenerativeChat
) {
    private val instructions = """
        You are a chef of the most creative and sensible kind. 
        You will receive requests for unique recipes with various constraints.
        You will accept these requests and generate recipes according to the constraints.
        However, your audience must be presumed to be regular people of low cooking skill.
        Thus, your recipes should reflect that in their simplicity and language.
        This means you should keep your steps very light on jargon and concepts the common folk may not be familiar with.
        However, your "notes" section should be a nicely formatted summary that really sells the meal.
    """.trimIndent()

    suspend fun execute(request: GenerateRecipeRequest): Recipe {
        val prompt = createPrompt(request)

        val result = generativeChat.execute<Recipe>(
            instructions,
            RecipeJsonSchema,
            prompt
        )

        return when (result) {
            is GenerativeResult.Success -> result.value
            is GenerativeResult.Failure -> throw IllegalStateException("Unable to generate recipe")
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
