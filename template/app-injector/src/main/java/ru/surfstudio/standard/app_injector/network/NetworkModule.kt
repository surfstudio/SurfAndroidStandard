package ru.surfstudio.standard.app_injector.network

import android.app.DownloadManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
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
import ru.surfstudio.android.network.calladapter.BaseCallAdapterFactory
import ru.surfstudio.android.template.app_injector.BuildConfig
import ru.surfstudio.standard.base.network.CallAdapterFactory
import ru.surfstudio.standard.i_network.BASE_API_URL

@Module
class NetworkModule {

    companion object {
        private const val HTTP_LOG_TAG = "OkHttp"
    }

    @Provides
    @PerApplication
    internal fun provideRetrofit(
            okHttpClient: OkHttpClient,
            callAdapterFactory: BaseCallAdapterFactory,
            gson: Gson,
            apiUrl: BaseUrl
    ): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiUrl.toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapterFactory(ResponseTypeAdapterFactory(SafeConverterFactory()))
                .create()
    }

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

    @Provides
    @PerApplication
    internal fun provideDownloadManager(context: Context): DownloadManager {
        return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    @Provides
    @PerApplication
    internal fun provideCallAdapterFactory(): BaseCallAdapterFactory = CallAdapterFactory()

    @Provides
    @PerApplication
    internal fun provideBaseUrl(): BaseUrl = BaseUrl(BASE_API_URL, null)
}