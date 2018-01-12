package ru.surfstudio.android.core.app.intialization;


import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.BuildConfig;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;

@Module
public class InitializationModule {

    public static final String VERSION_CODE_PARAM = "version_code";

    @Provides
    @PerApplication
    @Named(InitializationModule.VERSION_CODE_PARAM)
    public int provideVersionCode() {
        return BuildConfig.VERSION_CODE;
    }
}
