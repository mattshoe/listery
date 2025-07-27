package org.mattshoe.shoebox.listery.ai

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.util.RetryResult
import org.mattshoe.shoebox.listery.util.retry
import javax.inject.Inject

class GenerativeChat @Inject constructor(
    private val openAI: OpenAI
) {
    private val chatModel = ModelId("o4-mini-2025-04-16")

    internal suspend inline fun <reified T> execute(
        instructions: String,
        jsonSchema: String,
        prompt: String,
    ): GenerativeResult<T> {
        return try {
            val chatCompletionRequest = ChatCompletionRequest(
                model = chatModel,
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.System,
                        instructions + """
                            
                            Please follow the instructions above and return your response as a valid JSON object matching EXACTLY this schema:
                            $jsonSchema
                            
                            If you receive a prompt that does not make sense or has logical fallacies preventing you from fulfilling the request, please return exactly this JSON with an explanation of the reason you cannot fulfill the prompt. 
                            This text will be directly visible as "error" text for users of my application, so please keep it very short, succinct, and user-friendly:
                            { "explanation": String }
                            
                            Important Guidelines:
                            - Ensure the JSON is valid and properly formatted.
                            - Do not surround the JSON with markdown. Only plain text.
                            - Return only the JSON object, no other text.
                        """.trimIndent()
                    ),
                    ChatMessage(
                        role = ChatRole.User,
                        content = prompt
                    )
                )
            )
            val retryResult = retry(2) {
                executeChat<T>(chatCompletionRequest)
            }

            when (retryResult) {
                is RetryResult.Success -> GenerativeResult.Success(retryResult.value)
                is RetryResult.Failure -> executeChat<GenerativeError>(chatCompletionRequest).let {
                    GenerativeResult.Failure(it)
                }
            }
        } catch (e: Throwable) {
            loge(e)
            GenerativeResult.Failure(
                GenerativeError("Our servers ran into some issues, please try again in a few minutes.")
            )
        }
    }

    private suspend inline fun <reified T> executeChat(request: ChatCompletionRequest): T {
        val response = openAI.chatCompletion(request)
        val jsonResponse = response.choices.firstOrNull()?.message?.content
            ?: throw Exception("Failed to parse recipe")


        return Json.decodeFromString<T>(jsonResponse)
    }
}

sealed interface GenerativeResult<T> {
    data class Success<T>(val value: T): GenerativeResult<T>
    data class Failure<T>(val reason: GenerativeError): GenerativeResult<T>
}

@Serializable
data class GenerativeError(val explanation: String)