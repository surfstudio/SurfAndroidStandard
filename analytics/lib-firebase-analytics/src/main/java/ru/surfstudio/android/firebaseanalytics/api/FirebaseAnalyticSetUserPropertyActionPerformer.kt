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

import com.google.firebase.analytics.FirebaseAnalytics
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer

/**
 * Выполнитель действия setUserProperty на аналитике FirebaseAnalytics
 */
class FirebaseAnalyticSetUserPropertyActionPerformer(private val firebaseAnalytics: FirebaseAnalytics)
    : AnalyticActionPerformer<FirebaseAnalyticSetUserPropertyAction> {

    override fun canHandle(action: AnalyticAction) = action is FirebaseAnalyticSetUserPropertyAction

    override fun perform(action: FirebaseAnalyticSetUserPropertyAction) {
        firebaseAnalytics.setUserProperty(action.key.take(MAX_SET_USER_PROPERTY_KEY_LENGTH), action.value.take(MAX_SET_USER_PROPERTY_VALUE_LENGTH))
    }
}