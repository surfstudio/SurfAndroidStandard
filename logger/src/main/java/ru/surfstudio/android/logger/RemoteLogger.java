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

import java.util.ArrayList;
import java.util.List;

import ru.surfstudio.android.logger.remote_logging_strategies.RemoteLoggingStrategy;
import ru.surfstudio.android.logger.remote_logging_strategies.impl.crashlytics.CrashlyticsRemoteLoggingStrategy;

/**
 * Логгирует на удаленный сервер
 */
public class RemoteLogger {

    private static final RemoteLoggingStrategy DEFAULT_REMOTE_LOGGING_STRATEGY = new CrashlyticsRemoteLoggingStrategy();
    private static final List<RemoteLoggingStrategy> REMOTE_LOGGING_STRATEGIES = new ArrayList<>();

    private RemoteLogger() {
        // do nothing
    }

    public static void addRemoteLoggingStrategy(RemoteLoggingStrategy strategy) {
        REMOTE_LOGGING_STRATEGIES.add(strategy);
    }

    public static void setUser(String id, String username, String email) {
        forEachRemoteLoggingStrategyOrWithDefault(strategy -> strategy.setUser(id, username, email));
    }

    public static void clearUser() {
        forEachRemoteLoggingStrategyOrWithDefault(strategy -> strategy.clearUser());
    }

    public static void setCustomKey(String key, String value) {
        forEachRemoteLoggingStrategyOrWithDefault(strategy -> strategy.logKeyValue(key, value));
    }

    public static void logError(Throwable e) {
        forEachRemoteLoggingStrategyOrWithDefault(strategy -> strategy.logError(e));
    }

    public static void logMessage(String message) {
        forEachRemoteLoggingStrategyOrWithDefault(strategy -> strategy.logMessage(message));
    }

    private static void forEachRemoteLoggingStrategyOrWithDefault(ActionWithParam<RemoteLoggingStrategy> action) {
        if (REMOTE_LOGGING_STRATEGIES.isEmpty()) {
            action.perform(DEFAULT_REMOTE_LOGGING_STRATEGY);
            return;
        }

        for (RemoteLoggingStrategy strategy : REMOTE_LOGGING_STRATEGIES) {
            action.perform(strategy);
        }
    }
}
