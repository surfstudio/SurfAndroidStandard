package ru.surfstudio.android.firebaseanalytics.api

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analytics.Analytics


/**
 * Аналитика Firebase
 */
open class FirebaseApi(private val firebaseAnalytics: FirebaseAnalytics) : Analytics {

    /**
     * Максимально допустимая длинна значения параметра события.
     * Если ключ параметра больше, то он обрезается до этого значения
     */
    private val MAX_VALUE_LENGTH = 36

    override fun sendEvent(event: String) {
        firebaseAnalytics.logEvent(event, null)
    }

    override fun sendEvent(event: String, params: Map<String, String>) {
        val bundle = Bundle()
        params.forEach{ value -> bundle.putString(value.key, cut(value.value)) }
        firebaseAnalytics.logEvent(event, bundle)
    }

    override fun setUserProperty(key: String, value: String) {
        firebaseAnalytics.setUserProperty(key, cut(value))
    }

    private fun cut(source: String): String {
        return cut(source, MAX_VALUE_LENGTH)
    }

    private fun cut(source: String, maxLength: Int): String {
        if (source.isEmpty()) {
            return source
        }
        return if (source.length > maxLength)
            source.substring(0, maxLength)
        else
            source
    }
}