package ru.surfstudio.android.core.ui.base.event.delegate;


import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

public interface ScreenEventDelegateManager {
    void registerDelegate(ScreenEventDelegate delegate);

    void registerDelegate(ScreenEventDelegate delegate, @Nullable ScreenType emitterType);

    boolean unregisterDelegate(ScreenEventDelegate delegate);

    <E extends ScreenEvent, D extends ScreenEventDelegate, R> R sendEvent(E event);

    void destroy();
}
