package ru.surfstudio.standard.base.logger

/**
 * Interface of logging strategies for remote server
 */
interface RemoteLoggingStrategy {

    fun setUser(id: String?, username: String?, email: String?)

    fun clearUser()

    fun logError(e: Throwable?)

    fun logMessage(message: String?)

    fun logKeyValue(key: String?, value: String?)
}