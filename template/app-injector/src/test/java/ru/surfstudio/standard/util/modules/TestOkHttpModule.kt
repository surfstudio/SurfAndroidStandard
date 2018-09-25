package ru.surfstudio.standard.util.modules

import ru.surfstudio.standard.i_network.service.ServiceInterceptor
import ru.surfstudio.standard.i_token.TokenStorage
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.cache.SimpleCacheInterceptor
import ru.surfstudio.android.network.etag.EtagInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named

const val DI_NAME_SERVICE_INTERCEPTOR = "DI_NAME_SERVICE_INTERCEPTOR"
private const val NETWORK_TIMEOUT = 30L //sec

@Module
class TestOkHttpModule {

    @Provides
    @PerApplication
    @Named(DI_NAME_SERVICE_INTERCEPTOR)
    internal fun provideServiceInterceptor(tokenStorage: TokenStorage): Interceptor {
        return ServiceInterceptor(tokenStorage)
    }

    @Provides
    @PerApplication
    internal fun provideOkHttpClient(@Named(DI_NAME_SERVICE_INTERCEPTOR) serviceInterceptor: Interceptor,
                                     cacheInterceptor: SimpleCacheInterceptor,
                                     etagInterceptor: EtagInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

        okHttpClientBuilder.addInterceptor(cacheInterceptor)
        okHttpClientBuilder.addInterceptor(etagInterceptor)
        okHttpClientBuilder.addInterceptor(serviceInterceptor)
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        return okHttpClientBuilder.build()
    }
}
