package ru.surfstudio.standard.app.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;

@Module
public class AppModule {
    private Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @PerApplication
    Context provideContext() {
        return appContext;
    }
}
