package ru.surfstudio.standard.app_injector.network

import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.template.app_injector.BuildConfig

private const val HTTP_LOG_TAG = "OkHttp"

@Module
class HttpLoggingModule {

    @Provides
    @PerApplication
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Logger.d("$HTTP_LOG_TAG $message")
        }.apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.BASIC
        }
    }
}