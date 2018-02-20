package ru.surfstudio.android.core.app.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.ActiveActivityHolder;
import ru.surfstudio.android.core.app.CoreApp;
import ru.surfstudio.android.dagger.scope.PerApplication;


@Module
public class CoreAppModule {
    private CoreApp coreApp;

    public CoreAppModule(CoreApp coreApp) {
        this.coreApp = coreApp;
    }

    @PerApplication
    @Provides
    ActiveActivityHolder provideActiveActivityHolder() {
        return coreApp.getActiveActivityHolder();
    }

    @PerApplication
    @Provides
    Context provideContext() {
        return coreApp;
    }
}
