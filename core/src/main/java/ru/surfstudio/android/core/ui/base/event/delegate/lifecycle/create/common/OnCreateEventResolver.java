package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.common;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

public class OnCreateEventResolver extends MultipleScreenEventResolver<OnCreateEvent, OnCreateDelegate, Void> {
    @Override
    public Class<OnCreateDelegate> getDelegateType() {
        return OnCreateDelegate.class;
    }

    @Override
    public Class<OnCreateEvent> getEventType() {
        return OnCreateEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnCreateDelegate delegate, OnCreateEvent event) {
        delegate.onCreate();
        return null;
    }
}
