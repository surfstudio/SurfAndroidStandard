/*
  Copyright (c) 2020-present, SurfStudio LLC.

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
package ru.surfstudio.android.logger.remote_logging_strategies.impl.firebase_crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import ru.surfstudio.android.logger.remote_logging_strategies.RemoteLoggingStrategy

/**
 * Стратегия логгирования в Firebase Crashlytics
 */
class FirebaseCrashlyticsRemoteLoggingStrategy : RemoteLoggingStrategy {

    override fun setUser(id: String, username: String, email: String) {
        try {
            FirebaseCrashlytics.getInstance().setUserId(id)
        } catch (e: Exception) {
            //ignored
        }
    }

    override fun clearUser() {
        try {
            FirebaseCrashlytics.getInstance().setUserId("")
        } catch (e: Exception) {
            //ignored
        }
    }

    override fun logKeyValue(key: String, value: String) {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value)
        } catch (e: Exception) {
            //ignored
        }
    }

    override fun logError(e: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(e)
        } catch (err: Exception) {
            //ignored
        }
    }

    override fun logMessage(message: String) {
        try {
            FirebaseCrashlytics.getInstance().log(message)
        } catch (e: Exception) {
            //ignored
        }
    }
}