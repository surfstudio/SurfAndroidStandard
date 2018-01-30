package ru.surfstudio.android.logger;


import com.crashlytics.android.Crashlytics;

/**
 * Логгирует на удаленный сервер
 */
public class RemoteLogger {

    private RemoteLogger() {
        // do nothing
    }

    public static void setUser(String id, String username, String email) {
        try {
            Crashlytics.getInstance().core.setUserName(username);
            Crashlytics.getInstance().core.setUserEmail(email);
            Crashlytics.getInstance().core.setUserIdentifier(id);
        } catch (Exception e) {
            //ignored
        }
    }

    public static void clearUsername() {
        try {
            Crashlytics.getInstance().core.setUserName("");
            Crashlytics.getInstance().core.setUserEmail("");
            Crashlytics.getInstance().core.setUserIdentifier("");
        } catch (Exception e) {
            //ignored
        }
    }

    public static void setCustomKey(String key, String value) {
        try {
            Crashlytics.getInstance().core.setString(key, value);
        } catch (Exception e) {
            //ignored
        }
    }


    public static void logError(Throwable e) {
        try {
            Crashlytics.getInstance().core.logException(e);
        } catch (Exception err) {
            //ignored
        }
    }

    public static void logMessage(String message) {
        try {
            if (Crashlytics.getInstance() != null && message != null) {
                Crashlytics.getInstance().core.log(message);
            }
        } catch (Exception e) {
            //ignored
        }
    }
}
