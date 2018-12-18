package ru.surfstudio.android.firebaseanalytics.api

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analyticsv2.DefaultAnalyticActionPerformerCreator
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer

/**
 * Утилитные extension для FirebaseAnalytics
 */

/**
 * Добавляет поддержку событий logEvent и setUserProperty для FirebaseAnalytics
 */
fun <Action : AnalyticAction> DefaultAnalyticActionPerformerCreator<Action>.configDefaultFireBaseActions(firebaseAnalytics: FirebaseAnalytics): DefaultAnalyticActionPerformerCreator<Action> {
    addActionPerformer(FirebaseAnalyticEventSender(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    addActionPerformer(FirebaseAnalyticSetUserPropertyActionPerformer(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    return this
}

/**
 * Задает максимальную длину строки
 * Если больше знчения max, то обрезает её
 */
fun String.cut(maxLength: Int): String {
    if (isEmpty()) {
        return this
    }
    return if (this.length > maxLength)
        this.substring(0, maxLength)
    else
        this
}