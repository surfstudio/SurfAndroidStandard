package ru.surfstudio.android.core.mvp.error;

/**
 * интерфейс обработчика ошибок
 */
public interface ErrorHandler {
    void handleError(Throwable err);
}
