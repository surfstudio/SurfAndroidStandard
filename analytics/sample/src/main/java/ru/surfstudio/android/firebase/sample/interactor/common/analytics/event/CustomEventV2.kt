package ru.surfstudio.android.firebase.sample.interactor.common.analytics.event

import android.os.Bundle
import ru.surfstudio.android.firebaseanalytics.api.FirebaseAnalyticEvent

/**
 * Пример кастомного события
 */
class CustomEventV2(val value1: String,
                    val value2: Int,
                    val value3: Double) : FirebaseAnalyticEvent {

    override fun key() = "EventKey"

    override fun params() = Bundle().apply {
        putString("value_key_1", value1)
        putString("value_key_2", value2.toString())
        putString("value_key_3", value3.toString())
    }
}