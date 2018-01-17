package ru.surfstudio.android.core.ui.base.event.delegate;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

public interface ScreenEventDelegateManager {
    void registerDelegate(ScreenEventDelegate delegate);

    <E extends ScreenEvent, D extends ScreenEventDelegate, R> R sendEvent(E event);
}
