package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.ready;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
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
