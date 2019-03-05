/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.standard.i_push_notification.storage

import android.content.SharedPreferences
import ru.surfstudio.android.shared.pref.SettingsUtil

/**
 * Хранилище fcm-токена
 */
class FcmStorage(private val noBackupSharedPref: SharedPreferences) {

    companion object {
        private const val KEY_FCM_TOKEN = "FCM_TOKEN"
    }

    var fcmToken: String
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_FCM_TOKEN)
        set(value) = SettingsUtil.putString(noBackupSharedPref, KEY_FCM_TOKEN, value)

    fun clear() {
        SettingsUtil.putString(noBackupSharedPref, KEY_FCM_TOKEN, SettingsUtil.EMPTY_STRING_SETTING)
    }
}