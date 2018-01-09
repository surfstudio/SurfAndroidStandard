package ru.surfstudio.android.core.ui.base.delegate.manager;


import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;

/**
 * интерфейс менеджера {@link ScreenEventDelegate}
 */
public interface ScreenEventDelegateManager {
    void registerDelegate(ScreenEventDelegate delegate);
}
