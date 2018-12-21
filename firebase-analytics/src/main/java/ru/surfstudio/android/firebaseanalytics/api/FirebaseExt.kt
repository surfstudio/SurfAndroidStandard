package ru.surfstudio.android.firebaseanalytics.api

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analyticsv2.DefaultAnalyticActionPerformerCreator
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer

/**
 * Утилитные extension и константы для com.google.firebase.analytics.FirebaseAnalytics
 */

/**
 * Максимально допустимая длина имени события
 */
const val MAX_EVENT_KEY_LENGTH = 40
/**
 * Максмимально допустимая длинна значения параметра события
 */
const val MAX_EVENT_VALUE_LENGTH = 100

/**
 * Максимально допустимая длина ключа SetUserProperty
 */
const val MAX_SET_USER_PROPERTY_KEY_LENGTH = 24
/**
 * Максмимально допустимая длинна значения SetUserProperty
 */
const val MAX_SET_USER_PROPERTY_VALUE_LENGTH = 36


/**
 * Добавляет поддержку событий logEvent и setUserProperty для FirebaseAnalytics
 */
fun DefaultAnalyticActionPerformerCreator.configDefaultFireBaseActions(firebaseAnalytics: FirebaseAnalytics): DefaultAnalyticActionPerformerCreator {
    addActionPerformer(FirebaseAnalyticEventSender(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    addActionPerformer(FirebaseAnalyticSetUserPropertyActionPerformer(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    return this
}