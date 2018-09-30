package ru.surfstudio.android.network.sample.util.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.android.dagger.scope.PerApplication
import java.util.concurrent.TimeUnit

@Module
class TestOkHttpModule {

    companion object {
        private const val NETWORK_TIMEOUT = 10L //sec
    }

    @Provides
    @PerApplication
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

            addInterceptor(httpLoggingInterceptor)
        }.build()
    }
}