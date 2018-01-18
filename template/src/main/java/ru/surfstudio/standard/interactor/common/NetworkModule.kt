package ru.surfstudio.standard.interactor.common

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
import ru.surfstudio.android.core.BuildConfig
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.interactor.common.network.calladapter.BaseCallAdapterFactory
import ru.surfstudio.android.core.app.interactor.common.network.parse.ResponseTypeAdapterFactory
import ru.surfstudio.android.core.app.log.Logger
import javax.inject.Named

const val HTTP_LOG_TAG = "OkHttp"
const val DI_API_URL = "DI_API_URL"

@Module
class NetworkModule {

    @Provides
    @PerApplication
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 callAdapterFactory: BaseCallAdapterFactory,
                                 gson: Gson,
                                 @Named(DI_API_URL) apiUrl: String): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideGson(responseTypeAdapterFactory: ResponseTypeAdapterFactory): Gson {
        return GsonBuilder()
                .registerTypeAdapterFactory(responseTypeAdapterFactory)
                .create()
    }

    @Provides
    @PerApplication
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor { message -> Logger.d(HTTP_LOG_TAG, message) }
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