package ru.surfstudio.standard.interactor.common.network

import android.app.DownloadManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.network.BaseUrl
import ru.surfstudio.android.network.calladapter.BaseCallAdapterFactory
import ru.surfstudio.standard.BuildConfig

const val HTTP_LOG_TAG = "OkHttp"

@Module
class NetworkModule {

    @Provides
    @PerApplication
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 callAdapterFactory: BaseCallAdapterFactory,
                                 gson: Gson,
                                 apiUrl: BaseUrl): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiUrl.toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideGson(responseTypeAdapterFactory: ru.surfstudio.android.converter.gson.ResponseTypeAdapterFactory): Gson {
        return GsonBuilder()
                .registerTypeAdapterFactory(responseTypeAdapterFactory)
                .create()
    }

    @Provides
    @PerApplication
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor { message -> Logger.d("$HTTP_LOG_TAG $message") }
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        return logging
    }

    @Provides
    @PerApplication
    internal fun provideDownloadManager(context: Context): DownloadManager {
        return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

}