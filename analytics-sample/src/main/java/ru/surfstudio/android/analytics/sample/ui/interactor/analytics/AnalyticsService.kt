package ru.surfstudio.android.analytics.sample.ui.interactor.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analytics.Analytics
import ru.surfstudio.android.analytics.BaseAnalyticsService
import ru.surfstudio.android.analytics.sample.ui.interactor.analytics.event.CustomEvent
import ru.surfstudio.android.analytics.sample.ui.interactor.analytics.param.CommonParam
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

@PerApplication
class AnalyticsService @Inject constructor(apiStore: Analytics) : BaseAnalyticsService(apiStore) {

    fun trackEvent(stringValue: String) {
        sendEvent(CustomEvent()
                .add(CommonParam.COMMON_PARAM, "common_param_value")
                .add(FirebaseAnalytics.Param.ITEM_NAME, stringValue))
    }
}