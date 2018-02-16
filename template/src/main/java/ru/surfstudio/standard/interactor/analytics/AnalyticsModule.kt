package ru.surfstudio.standard.interactor.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.analytics.Analytics
import ru.surfstudio.android.analytics.store.AnalyticsStore
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.firebaseanalytics.api.FirebaseApi

/**
 * Пример dagger-модуля аналитики
 */
@Module
class AnalyticsModule {

    @Provides
    @PerApplication
    internal fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @PerApplication
    internal fun provideFirebaseApi(firebaseAnalytics: FirebaseAnalytics): FirebaseApi {
        return FirebaseApi(firebaseAnalytics)
    }

    @Provides
    @PerApplication
    internal fun provideAnalyticsStorage(firebaseApi: FirebaseApi): Analytics {
        return AnalyticsStore(listOf(firebaseApi))
    }

}