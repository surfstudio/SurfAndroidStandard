package ru.surfstudio.android.core.mvp.view;

/**
 * интерфейс для вью, поддерживающей обработку ошибок
 */
public interface HandleableErrorView {

    void handleError(Throwable error);
}
