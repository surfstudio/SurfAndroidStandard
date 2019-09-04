package ru.surfstudio.android.logger.remote_logging_strategies.impl.crashlytics

import com.crashlytics.android.Crashlytics

import ru.surfstudio.android.logger.remote_logging_strategies.RemoteLoggingStrategy

/**
 * Стратегия логгирования в Crashlytics
 */
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
