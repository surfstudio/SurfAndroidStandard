package ru.surfstudio.android.logger.remote_logging_strategies;

public interface RemoteLoggingStrategy {

    void setUser(String id, String username, String email);

    void clearUser();

    void logError(Throwable e);

    void logMessage(String message);

    void logKeyValue(String key, String value);
}
