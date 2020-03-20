package ru.surfstudio.standard.application.logger.strategies.remote

import android.util.Log

import ru.surfstudio.standard.base.logger.RemoteLogger
import ru.surfstudio.android.logger.logging_strategies.LoggingStrategy

/**
 * Logging strategy for [RemoteLogger] usage for messages with priority since Log.DEBUG
 */
class RemoteLoggerLoggingStrategy : LoggingStrategy {

    private val explicitTag = ThreadLocal<String>()

    private val tag: String?
        get() {
            val tag = explicitTag.get()
            if (tag != null) {
                explicitTag.remove()
            }
            return tag
        }

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        if (priority < MINIMAL_LOG_PRIORITY) {
            return
        }

        if (message != null) {
            RemoteLogger.logMessage(String.format(REMOTE_LOGGER_LOG_FORMAT, tag, message))
        }

        if (t != null && priority >= Log.ERROR) {
            RemoteLogger.logError(t)
        }
    }

    companion object {

        private val REMOTE_LOGGER_LOG_FORMAT = "%s: %s"
        private val MINIMAL_LOG_PRIORITY = Log.DEBUG
    }
}
