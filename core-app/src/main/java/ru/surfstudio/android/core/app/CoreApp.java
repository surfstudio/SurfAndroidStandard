package ru.surfstudio.android.core.app;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.github.anrwatchdog.ANRWatchDog;

import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.logger.RemoteLogger;

/**
 * Базовый класс приложения
 */

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
    private void initAnrWatchDog() {
        new ANRWatchDog().setReportMainThreadOnly()
                .setANRListener(RemoteLogger::logError)
                .start();
    }

    private void initLog() {
        Logger.init();
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    private void registerActiveActivityListener() {
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
