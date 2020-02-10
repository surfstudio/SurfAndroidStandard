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

private const val DEFAULT_STRING_VALUE = "null"

/**
 * Logging strategy for Firebase Crashlytics
 */
class FirebaseCrashlyticsRemoteLoggingStrategy : RemoteLoggingStrategy {

    override fun setUser(id: String?, username: String?, email: String?) {
        try {
            FirebaseCrashlytics.getInstance().setUserId(id ?: DEFAULT_STRING_VALUE)
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

    override fun logError(e: Throwable?) {
        try {
            if (e != null) {
                FirebaseCrashlytics.getInstance().recordException(e)
            } else {
                logMessage("FirebaseCrashlytics is ignoring a request to log a null exception.")
            }
        } catch (err: Exception) {
            //ignored
        }
    }

    override fun logMessage(message: String?) {
        try {
            FirebaseCrashlytics.getInstance().log(message ?: DEFAULT_STRING_VALUE)
        } catch (e: Exception) {
            //ignored
        }
    }

    override fun logKeyValue(key: String?, value: String?) {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey(
                    key ?: DEFAULT_STRING_VALUE,
                    value ?: DEFAULT_STRING_VALUE
            )
        } catch (e: Exception) {
            //ignored
        }
    }
}