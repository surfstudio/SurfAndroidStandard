package ru.surfstudio.android.core.app.interactor.common.network;

import android.app.DownloadManager;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.surfstudio.android.core.BuildConfig;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.interactor.common.network.calladapter.BaseCallAdapterFactory;
import ru.surfstudio.android.core.app.interactor.common.network.parse.ResponseTypeAdapterFactory;
import ru.surfstudio.android.core.app.log.Logger;

@Module
public class NetworkModule {

    public static final String HTTP_LOG_TAG = "OkHttp";
    public static final String DI_API_URL = "DI_API_URL";

    @Provides
    @PerApplication
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             BaseCallAdapterFactory callAdapterFactory,
                             Gson gson,
                             @Named(DI_API_URL) String apiUrl) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build();
    }

    @Provides
    @PerApplication
    Gson provideGson(ResponseTypeAdapterFactory responseTypeAdapterFactory) {
        return new GsonBuilder()
                .registerTypeAdapterFactory(responseTypeAdapterFactory)
                .create();
    }

    @Provides
    @PerApplication
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Logger.d(HTTP_LOG_TAG, message));
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return logging;
    }

    @Provides
    @PerApplication
    DownloadManager provideDownloadManager(Context context) {
        return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }
}