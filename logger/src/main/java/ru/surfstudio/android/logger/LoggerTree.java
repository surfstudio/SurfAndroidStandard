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
package ru.surfstudio.android.logger;

import android.util.Log;

import timber.log.Timber;

/**
 * логгирует в logcat
 * все логи начиная с уровня DEBUG логгируются в {@link RemoteLogger}
 */
public class LoggerTree extends Timber.DebugTree {

    public static final String REMOTE_LOGGER_LOG_FORMAT = "%s: %s";
    public static final String REMOTE_LOGGER_SEND_LOG_ERROR = "error when send developerMessage to RemoteLogger";
    private final int mLogPriority;

    /**
     * Создание экземпляра с приоритетом по умолчанию для логгирования в {@link RemoteLogger}.
     * По умолчанию приоритет - DEBUG.
     */
    public LoggerTree() {
        this(Log.DEBUG);
    }

    private LoggerTree(int logPriority) {
        mLogPriority = logPriority;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        super.log(priority, tag, message, t);
        try {
            if (priority >= mLogPriority) {
                RemoteLogger.logMessage(String.format(REMOTE_LOGGER_LOG_FORMAT, tag, message));
                if (t != null && priority >= Log.ERROR) {
                    RemoteLogger.logError(t);
                }
            }
        } catch (Exception e) {
            super.log(priority, tag, "Remote logger error", t);
        }
    }
}