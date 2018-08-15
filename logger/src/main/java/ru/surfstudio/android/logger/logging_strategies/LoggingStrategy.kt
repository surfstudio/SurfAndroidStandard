package ru.surfstudio.android.logger.logging_strategies

interface LoggingStrategy {

    fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?)
}
