package ru.surfstudio.android.network.sample.interactor.common.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.cache.SimpleCacheInterceptor
import ru.surfstudio.android.network.etag.EtagInterceptor
import java.util.concurrent.TimeUnit

@Module
class OkHttpModule {

    companion object {
        private const val NETWORK_TIMEOUT = 10L //sec
    }

    @Provides
    @PerApplication
    fun provideOkHttpClient(cacheInterceptor: SimpleCacheInterceptor,
                            etagInterceptor: EtagInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

            addInterceptor(cacheInterceptor)
            addInterceptor(etagInterceptor)
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }
}