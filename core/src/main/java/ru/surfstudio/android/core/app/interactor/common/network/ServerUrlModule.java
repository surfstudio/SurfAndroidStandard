package ru.surfstudio.android.core.app.interactor.common.network;


import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;

/**
 * Модуль предоставления url сервера
 */
@Module
public class ServerUrlModule {

    @Provides
    @PerApplication
    String provideBaseApiUrl() {
        //todo

//        return BuildConfig.USE_PRODUCTION_URL
//                ? ServerUrls.PRODUCTION_API_URL
//                : ServerUrls.TEST_API_URL;
        return "";
    }
}
