package ru.surf.android.notification.interactor.push

import android.content.Intent

import com.annimon.stream.Stream

import java.util.HashMap

/**
 * Конвертирует данные пуша из интента в мапу
 */
object IntentPushDataConverter {

    fun convert(intent: Intent): Map<String, String> {
        if (intent.extras == null) {
            return HashMap()
        }
        val data = HashMap<String, String>(intent.extras!!.size())
        Stream.of(intent.extras!!.keySet()).forEach { value: String -> data[value] = intent.extras!!.get(value)!!.toString() }
        return data
    }
}
