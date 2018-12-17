package ru.surfstudio.android.firebaseanalytics.api

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer

/**
 * Выполнитель действия setUserProperty на аналитике FirebaseAnalytics
 */
class FirebaseAnalyticSetUserPropertyActionPerformer(private val firebaseAnalytics: FirebaseAnalytics)
    : AnalyticActionPerformer<FirebaseAnalyticSetUserPropertyAction> {

    override fun perform(action: FirebaseAnalyticSetUserPropertyAction) {
        firebaseAnalytics.setUserProperty(action.key.cut(24), action.value.cut(36))
    }
}