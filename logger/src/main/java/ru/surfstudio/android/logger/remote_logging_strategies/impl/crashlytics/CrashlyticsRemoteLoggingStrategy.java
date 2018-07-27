package ru.surfstudio.android.logger.remote_logging_strategies.impl.crashlytics;

import com.crashlytics.android.Crashlytics;

import ru.surfstudio.android.logger.remote_logging_strategies.RemoteLoggingStrategy;

public class CrashlyticsRemoteLoggingStrategy implements RemoteLoggingStrategy {

    @Override
    public void setUser(String id, String username, String email) {
        try {
            Crashlytics.getInstance().core.setUserName(username);
            Crashlytics.getInstance().core.setUserEmail(email);
            Crashlytics.getInstance().core.setUserIdentifier(id);
        } catch (Exception e) {
            //ignored
        }
    }

    @Override
    public void clearUser() {
        try {
            Crashlytics.getInstance().core.setUserName("");
            Crashlytics.getInstance().core.setUserEmail("");
            Crashlytics.getInstance().core.setUserIdentifier("");
        } catch (Exception e) {
            //ignored
        }
    }

    @Override
    public void logKeyValue(String key, String value) {
        try {
            Crashlytics.getInstance().core.setString(key, value);
        } catch (Exception e) {
            //ignored
        }
    }

    @Override
    public void logError(Throwable e) {
        try {
            Crashlytics.getInstance().core.logException(e);
        } catch (Exception err) {
            //ignored
        }
    }

    @Override
    public void logMessage(String message) {
        try {
            if (Crashlytics.getInstance() != null && message != null) {
                Crashlytics.getInstance().core.log(message);
            }
        } catch (Exception e) {
            //ignored
        }
    }
}
