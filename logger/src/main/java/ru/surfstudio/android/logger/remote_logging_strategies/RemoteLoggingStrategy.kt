package ru.surfstudio.android.logger.remote_logging_strategies

/**
 * Интерфейс для стратегий логгирования на удаленный сервер
 */
interface RemoteLoggingStrategy {

    fun setUser(id: String?, username: String?, email: String?)

    fun clearUser()

    fun logError(e: Throwable?)

    fun logMessage(message: String?)

    fun logKeyValue(key: String?, value: String?)
}
