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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.surfstudio.android.logger.logging_strategies.LoggingStrategy;
import ru.surfstudio.android.logger.logging_strategies.impl.concrete.timber.TimberLoggingStrategy;

/**
 * логгирует в logcat
 * все логи начиная с уровня DEBUG логгируются в {@link RemoteLogger}
 */
public class Logger {

    private static final LoggingStrategy DEFAULT_LOGGING_STRATEGY = new TimberLoggingStrategy();
    private static final List<LoggingStrategy> LOGGING_STRATEGIES = new ArrayList<>();

    private Logger() {
        //do nothing
    }

    public static void addLoggingStrategy(LoggingStrategy loggingStrategy) {
        LOGGING_STRATEGIES.add(loggingStrategy);
    }

    /**
     * Log a verbose developerMessage with optional format args.
     */
    public static void v(@NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.v(message, args));
    }

    /**
     * Log a verbose exception and a developerMessage with optional format args.
     */
    public static void v(Throwable t, @NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.v(t, message, args));
    }

    /**
     * Log a debug developerMessage with optional format args.
     */
    public static void d(@NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.d(message, args));
    }

    /**
     * Log a debug exception and a developerMessage with optional format args.
     */
    public static void d(Throwable t, @NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.d(t, message, args));
    }

    /**
     * Log an info developerMessage with optional format args.
     */
    public static void i(@NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.i(message, args));
    }

    /**
     * Log an info exception and a developerMessage with optional format args.
     */
    public static void i(Throwable t, @NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.i(t, message, args));
    }

    /**
     * используется для ожидаемых ошибок
     * Логгирует только сообщение ошибки
     *
     * @param e
     */
    public static void w(Throwable e) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.w(e));
    }

    /**
     * Log a warning developerMessage with optional format args.
     */
    public static void w(@NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.w(message, args));
    }

    /**
     * Log a warning exception and a developerMessage with optional format args.
     */
    public static void w(Throwable t, @NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.w(t, message, args));
    }

    /**
     * Log an error developerMessage with optional format args.
     */
    public static void e(@NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.e(message, args));
    }

    /**
     * Log an error exception and a developerMessage with optional format args.
     */
    public static void e(Throwable t, @NonNull String message, Object... args) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.e(t, message, args));
    }

    public static void e(Throwable t) {
        forEachLoggingStrategyOrWithDefault(strategy -> strategy.e(t));
    }

    private static void forEachLoggingStrategyOrWithDefault(ActionWithParam<LoggingStrategy> action) {
        if (LOGGING_STRATEGIES.isEmpty()) {
            action.perform(DEFAULT_LOGGING_STRATEGY);
            return;
        }

        for (LoggingStrategy strategy : LOGGING_STRATEGIES) {
            action.perform(strategy);
        }
    }
}