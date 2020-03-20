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