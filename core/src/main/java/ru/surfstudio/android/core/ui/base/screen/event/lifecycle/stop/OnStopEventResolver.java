package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.stop;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnStopEventResolver extends MultipleScreenEventResolver<OnStopEvent, OnStopDelegate, Void> {
    @Override
    public Class<OnStopDelegate> getDelegateType() {
        return OnStopDelegate.class;
    }

    @Override
    public Class<OnStopEvent> getEventType() {
        return OnStopEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnStopDelegate delegate, OnStopEvent event) {
        delegate.onStop();
        return null;
    }
}
