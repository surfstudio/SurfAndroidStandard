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

import java.util.ArrayList

import ru.surfstudio.android.logger.remote_logging_strategies.RemoteLoggingStrategy
import ru.surfstudio.android.logger.remote_logging_strategies.impl.crashlytics.CrashlyticsRemoteLoggingStrategy

/**
 * Логгирует на удаленный сервер
 */
object RemoteLogger {

    private val DEFAULT_REMOTE_LOGGING_STRATEGY = CrashlyticsRemoteLoggingStrategy()
    private val REMOTE_LOGGING_STRATEGIES = ArrayList<RemoteLoggingStrategy>()

    fun getRemoteLoggingStrategies() = REMOTE_LOGGING_STRATEGIES

    fun addRemoteLoggingStrategy(strategy: RemoteLoggingStrategy) = REMOTE_LOGGING_STRATEGIES.add(strategy)

    fun removeRemoteLoggingStrategies(strategy: RemoteLoggingStrategy) = REMOTE_LOGGING_STRATEGIES.remove(strategy)

    fun setUser(id: String, username: String, email: String) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.setUser(id, username, email) }
    }

    fun clearUser() {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.clearUser() }
    }

    fun setCustomKey(key: String, value: String) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.logKeyValue(key, value) }
    }

    fun logError(e: Throwable) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.logError(e) }
    }

    fun logMessage(message: String) {
        forEachRemoteLoggingStrategyOrWithDefault { strategy -> strategy.logMessage(message) }
    }

    private fun forEachRemoteLoggingStrategyOrWithDefault(action: (RemoteLoggingStrategy) -> Unit) {
        if (REMOTE_LOGGING_STRATEGIES.isEmpty()) {
            action(DEFAULT_REMOTE_LOGGING_STRATEGY)
        } else {
            REMOTE_LOGGING_STRATEGIES.forEach(action)
        }
    }
}