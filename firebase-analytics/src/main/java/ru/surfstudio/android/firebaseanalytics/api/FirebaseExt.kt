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
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer

/**
 * Утилитные extension и константы для com.google.firebase.analytics.FirebaseAnalytics
 */

/**
 * Максимально допустимая длина имени события
 */
const val MAX_EVENT_KEY_LENGTH = 40
/**
 * Максмимально допустимая длинна значения параметра события
 */
const val MAX_EVENT_VALUE_LENGTH = 100

/**
 * Максимально допустимая длина ключа SetUserProperty
 */
const val MAX_SET_USER_PROPERTY_KEY_LENGTH = 24
/**
 * Максмимально допустимая длинна значения SetUserProperty
 */
const val MAX_SET_USER_PROPERTY_VALUE_LENGTH = 36

/**
 * Добавляет поддержку событий logEvent и setUserProperty для FirebaseAnalytics
 */
fun DefaultAnalyticService.configDefaultFireBaseActions(firebaseAnalytics: FirebaseAnalytics): DefaultAnalyticService {
    addActionPerformer(FirebaseAnalyticEventSender(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    addActionPerformer(FirebaseAnalyticSetUserPropertyActionPerformer(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    return this
}