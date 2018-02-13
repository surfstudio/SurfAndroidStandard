package ru.surfstudio.standard.interactor.analytics

import ru.surfstudio.android.analytics.Analytics
import ru.surfstudio.android.analytics.BaseAnalyticsService
import ru.surfstudio.android.analytics.store.AnalyticsStore
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * Пример сервиса аналитики
 */
@PerApplication
class AnalyticsService @Inject constructor(apiStore: Analytics) : BaseAnalyticsService(apiStore) {

}