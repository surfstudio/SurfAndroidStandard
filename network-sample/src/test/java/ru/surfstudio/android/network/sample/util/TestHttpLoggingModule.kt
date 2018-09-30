package ru.surfstudio.android.network.sample.util

import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import ru.surfstudio.android.dagger.scope.PerApplication

@Module
class TestHttpLoggingModule {

    @Provides
    @PerApplication
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            println(message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}