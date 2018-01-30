package ru.surfstudio.android.core.app.connection;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.dagger.scope.PerApplication;

@Module
public class ConnectionModule {

    @Provides
    @PerApplication
    public ConnectionProvider provideConnectionProvider(Context context) {
        return new ConnectionProvider(context);
    }
}
