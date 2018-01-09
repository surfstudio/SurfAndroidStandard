package ru.surfstudio.android.core.ui.base.delegate;


import ru.surfstudio.android.core.ui.base.delegate.manager.ScreenEventDelegateManager;

/**
 * интерфейс для экрана, поддерживающего делегирование обработки событий экрана
 */
public interface SupportScreenEventDelegation {
    ScreenEventDelegateManager getScreenEventDelegateManager();
}