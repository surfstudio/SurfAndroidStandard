package ru.surfstudio.android.logger.logging_strategies.impl.concrete.timber;

import ru.surfstudio.android.logger.logging_strategies.impl.base.AbstractGroupedLoggingStrategy;
import timber.log.Timber;

public class TimberLoggingStrategy extends AbstractGroupedLoggingStrategy {

    @Override
    protected void log(int priority, Throwable t) {
        setClickableLink();
        Timber.log(priority, t);
    }

    @Override
    protected void log(int priority, String message, Object... args) {
        setClickableLink();
        Timber.log(priority, message, args);
    }

    @Override
    protected void log(int priority, Throwable t, String message, Object... args) {
        setClickableLink();
        Timber.log(priority, t, message, args);
    }

    private static void setClickableLink() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length < 2) {
            throw new IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        StackTraceElement element = stackTrace[2];
        String tagMsg = String.format("(%s:%s) ", element.getFileName(), element.getLineNumber());
        Timber.tag(tagMsg);
    }
}
