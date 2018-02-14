package ru.surfstudio.android.notification.interactor.push

import android.content.Intent
import java.util.*

/**
 * Конвертирует данные пуша из интента в мапу
 */
object IntentPushDataConverter {

    fun convert(intent: Intent): Map<String, String> {
        if (intent.extras == null) {
            return HashMap()
        }
        val data = HashMap<String, String>(intent.extras!!.size())
        intent.extras!!.keySet()
                .forEach { value: String -> data[value] = intent.extras!!.get(value)!!.toString() }
        return data
    }
}
