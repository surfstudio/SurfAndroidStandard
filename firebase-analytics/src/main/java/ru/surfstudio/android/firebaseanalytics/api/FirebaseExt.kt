package ru.surfstudio.android.firebaseanalytics.api

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analyticsv2.DefaultAnalyticActionPerformerFactory

/**
 * Утилитные extension для FirebaseAnalytics
 */

/**
 * Добавляет поддержку событий logEvent и setUserProperty для FirebaseAnalytics
 */
fun DefaultAnalyticActionPerformerFactory.configDefaultFireBaseActions(firebaseAnalytics: FirebaseAnalytics): DefaultAnalyticActionPerformerFactory {
    addActionPerformer(FirebaseAnalyticEvent(""), FirebaseAnalyticEventSender(firebaseAnalytics))
    addActionPerformer(FirebaseAnalyticSetUserPropertyAction("", ""), FirebaseAnalyticSetUserPropertyActionPerformer(firebaseAnalytics))
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