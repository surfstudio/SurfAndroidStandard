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
package ru.surfstudio.android.logger.logging_strategies.impl.timber

import ru.surfstudio.android.logger.logging_strategies.LoggingStrategy
import timber.log.Timber

/**
 * Стратегия для логгирования с использованием Timber
 */
class TimberLoggingStrategy : LoggingStrategy {

    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        setClickableLink()
        Timber.log(priority, t, message, *args)
    }

    private fun setClickableLink() {
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size < 2) {
            throw IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        val element = stackTrace[2]
        val tagMsg = String.format("(%s:%s) ", element.fileName, element.lineNumber)
        Timber.tag(tagMsg)
    }
}
