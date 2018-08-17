package ru.surfstudio.android.logger.logging_strategies.impl.base;

import android.support.annotation.NonNull;
import android.util.Log;

import ru.surfstudio.android.logger.logging_strategies.LoggingStrategy;

public abstract class AbstractGroupedLoggingStrategy implements LoggingStrategy {

    @Override
    public void v(@NonNull String message, Object... args) {
        log(Log.VERBOSE, message, args);
    }

    @Override
    public void v(Throwable t, @NonNull String message, Object... args) {
        log(Log.VERBOSE, t, message, args);
    }

    @Override
    public void d(@NonNull String message, Object... args) {
        log(Log.DEBUG, message, args);
    }

    @Override
    public void d(Throwable t, @NonNull String message, Object... args) {
        log(Log.DEBUG, t, message, args);
    }

    @Override
    public void i(@NonNull String message, Object... args) {
        log(Log.INFO, message, args);
    }

    @Override
    public void i(Throwable t, @NonNull String message, Object... args) {
        log(Log.INFO, t, message, args);
    }

    @Override
    public void w(Throwable t) {
        log(Log.WARN, t);
    }

    @Override
    public void w(@NonNull String message, Object... args) {
        log(Log.WARN, message, args);
    }

    @Override
    public void w(Throwable t, @NonNull String message, Object... args) {
        log(Log.WARN, t, message, args);
    }

    @Override
    public void e(Throwable t) {
        log(Log.ERROR, t);
    }

    @Override
    public void e(@NonNull String message, Object... args) {
        log(Log.ERROR, message, args);
    }

    @Override
    public void e(Throwable t, @NonNull String message, Object... args) {
        log(Log.ERROR, message, args);
    }

    protected abstract void log(int priority, Throwable t);

    protected abstract void log(int priority, String message, Object... args);

    protected abstract void log(int priority, Throwable t, String message, Object... args);
}
