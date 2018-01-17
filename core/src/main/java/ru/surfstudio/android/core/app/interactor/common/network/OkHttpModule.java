package ru.surfstudio.android.core.app.interactor.common.network;


import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.interactor.common.network.cache.SimpleCacheInterceptor;
import ru.surfstudio.android.core.app.interactor.common.network.etag.EtagInterceptor;

/**
 * этот модуль вынесен отдельно для возможности замены его при интеграционном тестировании
 */
@Module
public class OkHttpModule {
    public static final int NETWORK_TIMEOUT = 10; //sec
    public static final String DI_NAME_SERVICE_INTERCEPTOR = "DI_NAME_SERVICE_INTERCEPTOR";

    @Provides
    @PerApplication
    OkHttpClient provideOkHttpClient(@Named(DI_NAME_SERVICE_INTERCEPTOR) Interceptor serviceInterceptor,
                                     SimpleCacheInterceptor cacheInterceptor,
                                     EtagInterceptor etagInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);

        okHttpClientBuilder.addInterceptor(cacheInterceptor);
        okHttpClientBuilder.addInterceptor(etagInterceptor);
        okHttpClientBuilder.addInterceptor(serviceInterceptor);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        return okHttpClientBuilder.build();
    }
}
