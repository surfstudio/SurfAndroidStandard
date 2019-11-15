package ru.surfstudio.standard.application.network.di

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
import ru.surfstudio.standard.i_network.converter.gson.ResponseTypeAdapterFactory
import ru.surfstudio.standard.i_network.converter.gson.SafeConverterFactory
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.i_network.network.BaseUrl
import ru.surfstudio.android.template.base_feature.BuildConfig
import ru.surfstudio.standard.base.util.StringsProvider
import ru.surfstudio.standard.f_debug.injector.DebugAppInjector
import ru.surfstudio.standard.i_network.BASE_API_URL
import ru.surfstudio.standard.i_network.TEST_API_URL
import ru.surfstudio.standard.i_network.error.handler.BaseErrorHandler
import ru.surfstudio.standard.i_network.error.handler.LogErrorHandler
import ru.surfstudio.standard.i_network.network.calladapter.CallAdapterFactory

@Module
class NetworkModule {

    companion object {
        private const val HTTP_LOG_TAG = "OkHttp"
    }

    @Provides
    @PerApplication
    internal fun provideRetrofit(
            okHttpClient: OkHttpClient,
            callAdapterFactory: CallAdapterFactory,
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
    internal fun provideErrorHandler(): BaseErrorHandler = LogErrorHandler()

    @Provides
    @PerApplication
    internal fun provideCallAdapterFactory(
            stringsProvider: StringsProvider,
            errorHandler: BaseErrorHandler?
    ): CallAdapterFactory = CallAdapterFactory(stringsProvider, errorHandler)

    @Provides
    @PerApplication
    internal fun provideBaseUrl(): BaseUrl {
        return if (DebugAppInjector.debugInteractor.isTestServerEnabled) {
            BaseUrl(TEST_API_URL, null)
        } else {
            BaseUrl(BASE_API_URL, null)
        }
    }
}