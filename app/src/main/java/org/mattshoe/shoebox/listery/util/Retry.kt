package org.mattshoe.shoebox.listery.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

sealed interface RetryResult<T> {
    data class Success<T>(val value: T): RetryResult<T>
    data class Failure<T>(val error: Throwable): RetryResult<T>
}

suspend fun <T> retry(
    maxAttempts: Int = 3,
    delay: Duration = 100.milliseconds,
    action: suspend CoroutineScope.() -> T
): RetryResult<T> = coroutineScope {
    try {
        RetryResult.Success(
            action()
        )
    } catch (e: Throwable) {
        if (maxAttempts <= 1) {
            RetryResult.Failure(e)
        } else {
            delay(delay)
            retry(maxAttempts - 1, delay, action)
        }
    }
}
