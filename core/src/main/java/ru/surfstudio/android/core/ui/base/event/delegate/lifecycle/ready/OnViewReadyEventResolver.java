package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

public class OnViewReadyEventResolver extends MultipleScreenEventResolver<OnViewReadyEvent, OnViewReadyDelegate, Void> {
    @Override
    public Class<OnViewReadyDelegate> getDelegateType() {
        return OnViewReadyDelegate.class;
    }

    @Override
    public Class<OnViewReadyEvent> getEventType() {
        return OnViewReadyEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnViewReadyDelegate delegate, OnViewReadyEvent event) {
        delegate.onViewReady();
        return null;
    }
}
