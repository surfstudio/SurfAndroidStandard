package ru.surfstudio.android.core.ui.base.screen.view;

/**
 * интерфейс для вью, поддерживающей обработку ошибок
 */
public interface HandleableErrorView {

    void handleError(Throwable error);
}
