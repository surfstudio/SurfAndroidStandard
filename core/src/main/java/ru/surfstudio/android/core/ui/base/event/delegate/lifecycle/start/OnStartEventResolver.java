package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.start;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

public class OnStartEventResolver extends MultipleScreenEventResolver<OnStartEvent, OnStartDelegate, Void> {
    @Override
    public Class<OnStartDelegate> getDelegateType() {
        return OnStartDelegate.class;
    }

    @Override
    public Class<OnStartEvent> getEventType() {
        return OnStartEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnStartDelegate delegate, OnStartEvent event) {
        delegate.onStart();
        return null;
    }
}
