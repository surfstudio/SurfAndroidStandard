package ru.surfstudio.android.core.app.interactor.common.network;

import dagger.Module;

@Module
public class NetworkModule {

//    public static final String HTTP_LOG_TAG = "OkHttp";
//
//    @Provides
//    @PerApplication
//    Retrofit provideRetrofit(OkHttpClient okHttpClient,
//                             CallAdapterFactory callAdapterFactory,
//                             Gson gson,
//                             String apiUrl) {
//        return new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(apiUrl)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(callAdapterFactory)
//                .build();
//    }
//
//    @Provides
//    @PerApplication
//    Gson provideGson(ResponseTypeAdapterFactory responseTypeAdapterFactory) {
//        return new GsonBuilder()
//                .registerTypeAdapterFactory(responseTypeAdapterFactory)
//                .create();
//    }
//
//    @Provides
//    @PerApplication
//    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.tag(HTTP_LOG_TAG).d(message));
//        if (BuildConfig.DEBUG) {
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
//            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        }
//        return logging;
//    }
//
//    @Provides
//    @PerApplication
//    DownloadManager provideDownloadManager(Context context) {
//        return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//    }
//
//    @Provides
//    @PerApplication
//    RxDownloader provideDownloader(Context context, DownloadManager downloadManager) {
//        return new RxDownloader(context, downloadManager);
//    }
}