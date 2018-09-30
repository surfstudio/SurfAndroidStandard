package ru.surfstudio.android.network.sample.interactor.common.network

import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger

@Module
class HttpLoggingModule {

    companion object {
        private const val HTTP_LOG_TAG = "OkHttp"
    }

    @Provides
    @PerApplication
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Logger.d("$HTTP_LOG_TAG $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}