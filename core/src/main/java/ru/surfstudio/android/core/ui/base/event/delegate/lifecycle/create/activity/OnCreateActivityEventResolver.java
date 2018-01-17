package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.activity;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

public class OnCreateActivityEventResolver extends MultipleScreenEventResolver<OnCreateActivityEvent, OnCreateActivityDelegate, Void> {
    @Override
    public Class<OnCreateActivityDelegate> getDelegateType() {
        return OnCreateActivityDelegate.class;
    }

    @Override
    public Class<OnCreateActivityEvent> getEventType() {
        return OnCreateActivityEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_TYPES;
    }

    @Override
    protected Void resolve(OnCreateActivityDelegate delegate, OnCreateActivityEvent event) {
        delegate.onCreate(event.getActivity());
        return null;
    }
}
