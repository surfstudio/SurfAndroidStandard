package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.stop;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

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
