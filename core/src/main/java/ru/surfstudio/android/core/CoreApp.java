package ru.surfstudio.android.core;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.github.anrwatchdog.ANRWatchDog;

import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.core.app.log.RemoteLogger;
import ru.surfstudio.android.core.ui.base.screen.delegate.DefaultScreenDelegateFactory;
import ru.surfstudio.android.core.ui.base.screen.delegate.ScreenDelegateFactory;
import ru.surfstudio.android.core.util.ActiveActivityHolder;
import ru.surfstudio.android.core.util.DefaultActivityLifecycleCallbacks;

/**
 * Базовый класс приложения
 */

public abstract class CoreApp extends MultiDexApplication {

    private ActiveActivityHolder activeActivityHolder = new ActiveActivityHolder();
    private ScreenDelegateFactory screenDelegateFactory;

    public static ScreenDelegateFactory getScreenDelegateFactory(Context context) {
        return CoreApp.class.cast(context.getApplicationContext()).getScreenDelegateFactory();
    }

    public ActiveActivityHolder getActiveActivityHolder() {
        return activeActivityHolder;
    }

    public ScreenDelegateFactory getScreenDelegateFactory() {
        return screenDelegateFactory;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initScreenDelegateFactory();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initAnrWatchDog();
        initLog();
        registerActiveActivityListener();
    }

    private void initScreenDelegateFactory() {
        screenDelegateFactory = new DefaultScreenDelegateFactory();
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
