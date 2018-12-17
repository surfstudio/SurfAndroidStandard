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
@Deprecated("Use DefaultAnalyticsService")
open class FirebaseApi(private val firebaseAnalytics: FirebaseAnalytics) : Analytics {


    override fun sendEvent(event: String) {
        firebaseAnalytics.logEvent(event, null)
    }

    override fun sendEvent(event: String, params: Map<String, String>) {
        val bundle = Bundle()
        params.forEach { value -> bundle.putString(value.key.cut(40), value.value.cut(100)) }
        firebaseAnalytics.logEvent(event, bundle)
    }

    override fun setUserProperty(key: String, value: String) {
        firebaseAnalytics.setUserProperty(key.cut(24), value.cut(36))
    }
}