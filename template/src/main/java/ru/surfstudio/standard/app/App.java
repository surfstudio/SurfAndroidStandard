package ru.surfstudio.standard.app;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import ru.surfstudio.android.core.BaseApp;
import ru.surfstudio.standard.app.dagger.ActiveActivityHolderModule;
import ru.surfstudio.standard.app.dagger.AppComponent;
import ru.surfstudio.standard.app.dagger.AppModule;
import ru.surfstudio.standard.app.dagger.DaggerAppComponent;

/**
 * Класс приложения
 */
public class App extends BaseApp {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
        initFabric();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    private void initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .activeActivityHolderModule(new ActiveActivityHolderModule(getActiveActivityHolder()))
                .build();
    }

    private void initFabric() {
        final Kit[] kits = {
                new Crashlytics.Builder().core(new CrashlyticsCore.Builder().build()).build()
        };
        Fabric.with(this, kits);
    }
}