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
package ru.surfstudio.android.logger.remote_logging_strategies.impl.crashlytics

import com.crashlytics.android.Crashlytics

import ru.surfstudio.android.logger.remote_logging_strategies.RemoteLoggingStrategy

/**
 * Logging strategy for Fabric Crashlytics
 */
@Deprecated("Use FirebaseCrashlyticsRemoteLoggingStrategy and firebase crashlytics instead")
class CrashlyticsRemoteLoggingStrategy : RemoteLoggingStrategy {

    override fun setUser(id: String?, username: String?, email: String?) {
        try {
            Crashlytics.getInstance().core.setUserName(username)
            Crashlytics.getInstance().core.setUserEmail(email)
            Crashlytics.getInstance().core.setUserIdentifier(id)
        } catch (e: Exception) {
            //ignored
        }
    }

    override fun clearUser() {
        try {
            Crashlytics.getInstance().core.setUserName("")
            Crashlytics.getInstance().core.setUserEmail("")
            Crashlytics.getInstance().core.setUserIdentifier("")
        } catch (e: Exception) {
            //ignored
        }
    }

    override fun logKeyValue(key: String?, value: String?) {
        try {
            Crashlytics.getInstance().core.setString(key, value)
        } catch (e: Exception) {
            //ignored
        }
    }

    override fun logError(e: Throwable?) {
        try {
            Crashlytics.getInstance().core.logException(e)
        } catch (err: Exception) {
            //ignored
        }
    }

    override fun logMessage(message: String?) {
        try {
            Crashlytics.getInstance().core.log(message)
        } catch (e: Exception) {
            //ignored
        }
    }
}
