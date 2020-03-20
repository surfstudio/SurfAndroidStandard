package ru.surfstudio.standard.application.logger.strategies.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import ru.surfstudio.standard.base.logger.RemoteLoggingStrategy

private const val DEFAULT_STRING_VALUE = "null"

private const val DEFAULT_USERNAME_KEY = "username"
private const val DEFAULT_EMAIL_KEY = "email"

/**
 * Logging strategy for Firebase Crashlytics
 */
class FirebaseCrashlyticsRemoteLoggingStrategy : RemoteLoggingStrategy {

    override fun setUser(id: String?, username: String?, email: String?) {
        try {
            FirebaseCrashlytics.getInstance().setUserId(id ?: DEFAULT_STRING_VALUE)
            logKeyValue(DEFAULT_USERNAME_KEY, username)
            logKeyValue(DEFAULT_EMAIL_KEY, email)
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