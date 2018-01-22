package ru.surfstudio.standard.interactor.common.network


import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.network.cache.SimpleCacheInterceptor
import ru.surfstudio.android.network.etag.EtagInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named

const val DI_NAME_SERVICE_INTERCEPTOR = "DI_NAME_SERVICE_INTERCEPTOR"
private const val NETWORK_TIMEOUT = 10 //sec

/**
 * этот модуль вынесен отдельно для возможности замены его при интеграционном тестировании
 */
@Module
class OkHttpModule {

    @Provides
    @PerApplication
    internal fun provideOkHttpClient(@Named(DI_NAME_SERVICE_INTERCEPTOR) serviceInterceptor: Interceptor,
                                     cacheInterceptor: SimpleCacheInterceptor,
                                     etagInterceptor: EtagInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(NETWORK_TIMEOUT.toLong(), TimeUnit.SECONDS)

        okHttpClientBuilder.addInterceptor(cacheInterceptor)
        okHttpClientBuilder.addInterceptor(etagInterceptor)
        okHttpClientBuilder.addInterceptor(serviceInterceptor)
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        return okHttpClientBuilder.build()
    }
}
