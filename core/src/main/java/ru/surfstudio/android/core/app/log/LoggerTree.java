package ru.surfstudio.android.core.app.log;

import android.util.Log;

import timber.log.Timber;

/**
 * логгирует в logcat
 * все логи начиная с уровня DEBUG логгируются в {@link RemoteLogger}
 */
public class LoggerTree extends Timber.DebugTree {

    public static final String REMOTE_LOGGER_LOG_FORMAT = "%s: %s";
    public static final String REMOTE_LOGGER_SEND_LOG_ERROR = "error when send developerMessage to RemoteLogger";
    private final int mLogPriority;

    /**
     * Создание экземпляра с приоритетом по умолчанию для логгирования в {@link RemoteLogger}.
     * По умолчанию приоритет - DEBUG.
     */
    public LoggerTree() {
        this(Log.DEBUG);
    }

    private LoggerTree(int logPriority) {
        mLogPriority = logPriority;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        super.log(priority, tag, message, t);
        try {
            if (priority >= mLogPriority) {
                RemoteLogger.logMessage(String.format(REMOTE_LOGGER_LOG_FORMAT, tag, message));
                if (t != null && priority >= Log.ERROR) {
                    RemoteLogger.logError(t);
                }
            }
        } catch (Exception e) {
            super.log(priority, tag, "Remote logger error", t);
        }
    }
}