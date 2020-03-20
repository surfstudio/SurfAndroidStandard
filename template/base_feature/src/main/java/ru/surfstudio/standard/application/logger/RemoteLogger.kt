package ru.surfstudio.standard.application.logger

import ru.surfstudio.android.logger.remote_logging_strategies.RemoteLoggingStrategy
import kotlin.reflect.KClass

/**
 * Object for logging to remote server which supports different logging strategies
 */
object RemoteLogger {

    private val REMOTE_LOGGING_STRATEGIES = hashMapOf<KClass<*>, RemoteLoggingStrategy>()

    @JvmStatic
    fun getRemoteLoggingStrategies() = REMOTE_LOGGING_STRATEGIES

    @JvmStatic
    fun addRemoteLoggingStrategy(strategy: RemoteLoggingStrategy) =
            REMOTE_LOGGING_STRATEGIES.put(strategy::class, strategy)

    @JvmStatic
    fun removeRemoteLoggingStrategies(strategy: RemoteLoggingStrategy) =
            REMOTE_LOGGING_STRATEGIES.remove(strategy::class)

    @JvmStatic
    fun setUser(id: String, username: String, email: String) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.setUser(id, username, email) }
    }

    @JvmStatic
    fun clearUser() {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.clearUser() }
    }

    @JvmStatic
    fun setCustomKey(key: String, value: String) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.logKeyValue(key, value) }
    }

    @JvmStatic
    fun logError(e: Throwable) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.logError(e) }
    }

    @JvmStatic
    fun logMessage(message: String) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.logMessage(message) }
    }

    private fun forEachRemoteLoggingStrategyOrWithDefault(action: (RemoteLoggingStrategy) -> Unit) {
        REMOTE_LOGGING_STRATEGIES.values.forEach(action)
    }
}