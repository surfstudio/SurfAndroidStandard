package ru.surfstudio.android.firebase.sample.interactor.common.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analytics.Analytics
import ru.surfstudio.android.analytics.BaseAnalyticsService
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.event.CustomEvent
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.param.CommonParam
import javax.inject.Inject

@PerApplication
class AnalyticsService @Inject constructor(apiStore: Analytics) : BaseAnalyticsService(apiStore) {

    fun trackEvent(stringValue: String) {
        sendEvent(CustomEvent()
                .add(CommonParam.COMMON_PARAM, "common_param_value")
                .add(FirebaseAnalytics.Param.ITEM_NAME, stringValue))
    }
}