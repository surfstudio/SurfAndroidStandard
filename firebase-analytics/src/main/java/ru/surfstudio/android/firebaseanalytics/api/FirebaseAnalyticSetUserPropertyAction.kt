package ru.surfstudio.android.firebaseanalytics.api

import ru.surfstudio.android.analyticsv2.core.AnalyticAction

/**
 * Действие установки параметров setUserProperty для FireBaseAnalytics
 * @param key ключ userProperty
 * @param value значение userProperty
 */
open class FirebaseAnalyticSetUserPropertyAction(val key : String, val value: String) : AnalyticAction