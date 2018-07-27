package ru.surfstudio.android.logger.logging_strategies;

import android.support.annotation.NonNull;

public interface LoggingStrategy {

    void v(@NonNull String message, Object... args);

    void v(Throwable t, @NonNull String message, Object... args);

    void d(@NonNull String message, Object... args);

    void d(Throwable t, @NonNull String message, Object... args);

    void i(@NonNull String message, Object... args);

    void i(Throwable t, @NonNull String message, Object... args);

    void w(Throwable t);

    void w(@NonNull String message, Object... args);

    void w(Throwable t, @NonNull String message, Object... args);

    void e(Throwable t);

    void e(@NonNull String message, Object... args);

    void e(Throwable t, @NonNull String message, Object... args);
}
