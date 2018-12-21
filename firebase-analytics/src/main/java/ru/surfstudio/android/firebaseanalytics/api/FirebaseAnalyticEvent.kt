package ru.surfstudio.android.firebaseanalytics.api

import android.os.Bundle
import ru.surfstudio.android.analyticsv2.ActionKey
import ru.surfstudio.android.analyticsv2.core.AnalyticAction

/**
 * Событие Firebase аналитики
 * @param name ключ события (имя)
 * @param params параметры события (может не быть)
 */
interface FirebaseAnalyticEvent : AnalyticAction, ActionKey {
    /**
     * Параметры события
     */
    fun params(): Bundle
}