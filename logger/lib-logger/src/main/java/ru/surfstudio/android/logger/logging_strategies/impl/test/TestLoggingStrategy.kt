package ru.surfstudio.android.logger.logging_strategies.impl.test

import android.util.Log
import ru.surfstudio.android.logger.logging_strategies.LoggingStrategy
import java.io.PrintStream

/**
 * Стратегия логгирования для тестов, использующая системный поток вывода для логов
 */
class TestLoggingStrategy : LoggingStrategy {

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        val logStream = getLogStreamForPriority(priority)

        if (t != null) {
            logStream.println(t)
        }

        if (message != null) {
            logStream.println(message)
        }
    }

    private fun getLogStreamForPriority(priority: Int): PrintStream {
        return if (priority < Log.ERROR) {
            System.out
        } else {
            System.err
        }
    }
}