package org.mattshoe.shoebox.listery.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

sealed interface RetryResult<T> {
    data class Success<T>(val value: T): RetryResult<T>
    data class Failure<T>(val error: Throwable): RetryResult<T>
}

suspend fun <T> retry(
    maxAttempts: Int = 3,
    action: suspend CoroutineScope.() -> T
): RetryResult<T> = coroutineScope {
    try {
        RetryResult.Success(
            action()
        )
    } catch (e: Throwable) {
        if (maxAttempts < 1) {
            RetryResult.Failure(e)
        } else {
            retry(maxAttempts - 1, action)
        }
    }
}
