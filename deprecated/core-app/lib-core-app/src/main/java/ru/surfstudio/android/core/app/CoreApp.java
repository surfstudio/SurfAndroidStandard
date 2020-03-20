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
package ru.surfstudio.android.core.app;

import android.app.Activity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.github.anrwatchdog.ANRWatchDog;

import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.logger.RemoteLogger;
import ru.surfstudio.android.logger.logging_strategies.impl.remote_logger.RemoteLoggerLoggingStrategy;
import ru.surfstudio.android.logger.logging_strategies.impl.timber.TimberLoggingStrategy;

/**
 * Базовый класс приложения
 *
 * @deprecated реализуйте свою
 */
@Deprecated
public abstract class CoreApp extends MultiDexApplication {

    private ActiveActivityHolder activeActivityHolder = new ActiveActivityHolder();

    public ActiveActivityHolder getActiveActivityHolder() {
        return activeActivityHolder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initAnrWatchDog();
        initLog();
        registerActiveActivityListener();
    }

    /**
     * отслеживает ANR и отправляет в крашлитикс
     */
    protected void initAnrWatchDog() {
        new ANRWatchDog().setReportMainThreadOnly()
                .setANRListener(RemoteLogger::logError)
                .start();
    }

    protected void initLog() {
        Logger.addLoggingStrategy(new TimberLoggingStrategy());
        Logger.addLoggingStrategy(new RemoteLoggerLoggingStrategy());
        //todo setup Remote Logger
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    protected void registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(new DefaultActivityLifecycleCallbacks() {
            @Override
            public void onActivityResumed(Activity activity) {
                activeActivityHolder.setActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                activeActivityHolder.clearActivity();
            }
        });
    }
}