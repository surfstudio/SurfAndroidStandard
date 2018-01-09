package ru.surfstudio.standard.app;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.github.anrwatchdog.ANRWatchDog;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.core.app.log.RemoteLogger;
import ru.surfstudio.android.core.util.ActiveActivityHolder;
import ru.surfstudio.android.core.util.DefaultActivityLifecycleCallbacks;
import ru.surfstudio.standard.app.dagger.ActiveActivityHolderModule;
import ru.surfstudio.standard.app.dagger.AppComponent;
import ru.surfstudio.standard.app.dagger.AppModule;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Класс приложения
 */
public class App extends Application {

    private AppComponent appComponent;
    private ActiveActivityHolder activeActivityHolder = new ActiveActivityHolder();

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initFabric();
        initAnrWatchDog();
        initInjector();
        initLog();
        initCalligraphy();
        registerActiveActivityListener();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    /**
     * отслеживает ANR и отправляет в крашлитикс
     */
    private void initAnrWatchDog() {
        new ANRWatchDog().setReportMainThreadOnly()
                .setANRListener(RemoteLogger::logError)
                .start();
    }

    private void initFabric() {
        final Kit[] kits = {
                new Crashlytics.Builder().core(new CrashlyticsCore.Builder().build()).build()
        };
        Fabric.with(this, kits);
    }

    private void initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .activeActivityHolderModule(new ActiveActivityHolderModule(activeActivityHolder))
                .build();
    }

    private void initLog() {
        Logger.init();
    }

    /**
     * Инициализация шрифтовой библиотеки Calligraphy
     */
    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(ru.surfstudio.android.core.R.attr.fontPath)
                .build());
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