/*
  Copyright (c) 2020-present, SurfStudio LLC.

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
package ru.surfstudio.android.logger.logging_strategies.impl.remote_logger

import android.util.Log

import ru.surfstudio.android.logger.RemoteLogger
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
