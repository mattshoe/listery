package org.mattshoe.shoebox.listery.logging

import android.util.Log

/**
 * Simple logging utility that wraps Android's Logcat functionality.
 * Provides easy-to-use top-level functions for logging at different levels.
 */

enum class LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR
}

private const val DEFAULT_TAG = "MATTSHOE"

/**
 * Log this object at the specified level
 * @param message The message to log
 * @param level The log level (defaults to DEBUG)
 * @param tag The tag to use (defaults to "Listery")
 * @param throwable Optional throwable to log
 */
fun Any?.logthis(
    level: LogLevel = LogLevel.DEBUG,
    tag: String = DEFAULT_TAG,
    throwable: Throwable? = null
) {
    log(this, level, tag, throwable)
}

/**
 * Log a message at the specified level
 * @param message The message to log
 * @param level The log level (defaults to DEBUG)
 * @param tag The tag to use (defaults to "Listery")
 * @param throwable Optional throwable to log
 */
fun log(
    message: Any?,
    level: LogLevel = LogLevel.DEBUG,
    tag: String = DEFAULT_TAG,
    throwable: Throwable? = null
) {
    when (level) {
        LogLevel.VERBOSE -> Log.v(tag, message.toString(), throwable)
        LogLevel.DEBUG -> Log.d(tag, message.toString(), throwable)
        LogLevel.INFO -> Log.i(tag, message.toString(), throwable)
        LogLevel.WARN -> Log.w(tag, message.toString(), throwable)
        LogLevel.ERROR -> Log.e(tag, message.toString(), throwable)
    }
}

// Convenience functions for different log levels
fun logv(message: Any?, tag: String = DEFAULT_TAG, throwable: Throwable? = null) =
    log(message, LogLevel.VERBOSE, tag, throwable)

fun logd(message: Any?, tag: String = DEFAULT_TAG, throwable: Throwable? = null) =
    log(message, LogLevel.DEBUG, tag, throwable)

fun logi(message: Any?, tag: String = DEFAULT_TAG, throwable: Throwable? = null) =
    log(message, LogLevel.INFO, tag, throwable)

fun logw(message: Any?, tag: String = DEFAULT_TAG, throwable: Throwable? = null) =
    log(message, LogLevel.WARN, tag, throwable)

fun loge(message: Any?, throwable: Throwable? = null, tag: String = DEFAULT_TAG) =
    log(message, LogLevel.ERROR, tag, throwable)