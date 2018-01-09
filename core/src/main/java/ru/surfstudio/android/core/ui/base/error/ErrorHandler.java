package ru.surfstudio.android.core.ui.base.error;

/**
 * интерфейс обработчика ошибок
 */
public interface ErrorHandler {
    void handleError(Throwable err);
}
