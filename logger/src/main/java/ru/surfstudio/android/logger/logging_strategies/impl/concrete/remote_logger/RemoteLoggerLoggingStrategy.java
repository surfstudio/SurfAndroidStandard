package ru.surfstudio.android.logger.logging_strategies.impl.concrete.remote_logger;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import ru.surfstudio.android.logger.RemoteLogger;
import ru.surfstudio.android.logger.logging_strategies.impl.base.AbstractGroupedLoggingStrategy;

public class RemoteLoggerLoggingStrategy extends AbstractGroupedLoggingStrategy {

    private static final String REMOTE_LOGGER_LOG_FORMAT = "%s: %s";
    private static final int MINIMAL_LOG_PRIORITY = Log.DEBUG;

    private final ThreadLocal<String> explicitTag = new ThreadLocal<>();

    @Override
    protected void log(int priority, Throwable t) {
        log(priority, t, null);
    }

    @Override
    protected void log(int priority, String message, Object... args) {
        log(priority, null, message, args);
    }

    @Override
    protected void log(int priority, Throwable t, String message, Object... args) {
        if (priority < MINIMAL_LOG_PRIORITY) {
            return;
        }

        if (message != null) {
            RemoteLogger.logMessage(String.format(REMOTE_LOGGER_LOG_FORMAT, getTag(), message));
        }

        if (t != null && priority >= Log.ERROR) {
            RemoteLogger.logError(t);
        }
    }

    @Nullable
    private String getTag() {
        String tag = explicitTag.get();
        if (tag != null) {
            explicitTag.remove();
        }
        return tag;
    }
}
