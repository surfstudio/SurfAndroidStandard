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
package ru.surfstudio.android.logger.logging_strategies.impl.test

import android.util.Log
import ru.surfstudio.android.logger.logging_strategies.LoggingStrategy
import java.io.PrintStream

/**
 * Logging strategy for tests which uses system output for logs
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