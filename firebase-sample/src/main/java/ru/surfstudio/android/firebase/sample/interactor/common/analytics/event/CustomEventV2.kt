package ru.surfstudio.android.firebase.sample.interactor.common.analytics.event

import android.os.Bundle
import ru.surfstudio.android.firebaseanalytics.api.FirebaseAnalyticEvent

/**
 * Пример кастомного события
 *
 * //todo конвертация в старый EventData. Удобный мапинг старых событий в новую аналитику (в самих событиях по хорошему ничего менять не должно быть нужды)
 */
class CustomEventV2(val value1: String,
                    val value2: Int,
                    val value3: Double) : FirebaseAnalyticEvent {

    override fun key() = EventType.CUSTOM_EVENT_TYPE

    override fun params() = Bundle().apply {
        putString("value_key_1", value1)
        putInt("value_key_2", value2)
        putDouble("value_key_3", value3)
    }
}