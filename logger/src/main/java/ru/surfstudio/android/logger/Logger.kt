/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.logger

import android.util.Log
import java.util.ArrayList

import ru.surfstudio.android.logger.logging_strategies.LoggingStrategy
import ru.surfstudio.android.logger.logging_strategies.impl.concrete.timber.TimberLoggingStrategy

/**
 * логгирует в logcat
 * все логи начиная с уровня DEBUG логгируются в [RemoteLogger]
 */
object Logger {

    private val DEFAULT_LOGGING_STRATEGY = TimberLoggingStrategy()
    private val LOGGING_STRATEGIES = ArrayList<LoggingStrategy>()

    fun addLoggingStrategy(loggingStrategy: LoggingStrategy) = LOGGING_STRATEGIES.add(loggingStrategy)

    /**
     * Log a verbose developerMessage with optional format args.
     */
    fun v(message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.VERBOSE, null, message, *args) }
    }

    /**
     * Log a verbose exception and a developerMessage with optional format args.
     */
    fun v(t: Throwable, message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.VERBOSE, t, message, *args) }
    }

    /**
     * Log a debug developerMessage with optional format args.
     */
    fun d(message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.DEBUG, null, message, *args) }
    }

    /**
     * Log a debug exception and a developerMessage with optional format args.
     */
    fun d(t: Throwable, message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.DEBUG, t, message, *args) }
    }

    /**
     * Log an info developerMessage with optional format args.
     */
    fun i(message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.INFO, null, message, *args) }
    }

    /**
     * Log an info exception and a developerMessage with optional format args.
     */
    fun i(t: Throwable, message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.INFO, t, message, *args) }
    }

    /**
     * используется для ожидаемых ошибок
     * Логгирует только сообщение ошибки
     *
     * @param e
     */
    fun w(e: Throwable) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.WARN, e, null) }
    }

    /**
     * Log a warning developerMessage with optional format args.
     */
    fun w(message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.WARN, null, message, *args) }
    }

    /**
     * Log a warning exception and a developerMessage with optional format args.
     */
    fun w(t: Throwable, message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.WARN, t, message, *args) }
    }

    /**
     * Log an error developerMessage with optional format args.
     */
    fun e(message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.ERROR, null, message, *args) }
    }

    /**
     * Log an error exception and a developerMessage with optional format args.
     */
    fun e(t: Throwable, message: String, vararg args: Any) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.ERROR, t, message, *args) }
    }

    fun e(t: Throwable) {
        forEachLoggingStrategyOrWithDefault { strategy -> strategy.log(Log.ERROR, t, null) }
    }

    private fun forEachLoggingStrategyOrWithDefault(action: (LoggingStrategy) -> Unit) {
        if (LOGGING_STRATEGIES.isEmpty()) {
            action(DEFAULT_LOGGING_STRATEGY)
        } else {
            LOGGING_STRATEGIES.forEach(action)
        }
    }
}