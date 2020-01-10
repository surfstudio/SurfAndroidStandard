package ru.surfstudio.android.filestorage.sample.interactor.common.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.surfstudio.android.converter.gson.ResponseTypeAdapterFactory
import ru.surfstudio.android.converter.gson.SafeConverterFactory
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.network.BaseUrl
import ru.surfstudio.android.filestorage.sample.interactor.common.network.calladapter.CallAdapterFactory

@Module
class NetworkModule {

    companion object {
        private const val HTTP_LOG_TAG = "OkHttp"
    }

    @Provides
    @PerApplication
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        callAdapterFactory: CallAdapterFactory,
                        gson: Gson,
                        baseUrl: BaseUrl): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl.toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @PerApplication
    fun provideGson(responseTypeAdapterFactory: ResponseTypeAdapterFactory): Gson {
        return GsonBuilder()
                .registerTypeAdapterFactory(responseTypeAdapterFactory)
                .create()
    }

    @Provides
    @PerApplication
    fun provideResponseTypeAdapterFactory(): ResponseTypeAdapterFactory {
        return ResponseTypeAdapterFactory(SafeConverterFactory())
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