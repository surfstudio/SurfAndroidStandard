/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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