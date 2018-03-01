package ru.surfstudio.android.core.ui.event.lifecycle.pause;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnPauseEventResolver extends MultipleScreenEventResolver<OnPauseEvent, OnPauseDelegate, Void> {
    @Override
    public Class<OnPauseDelegate> getDelegateType() {
        return OnPauseDelegate.class;
    }

    @Override
    public Class<OnPauseEvent> getEventType() {
        return OnPauseEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnPauseDelegate delegate, OnPauseEvent event) {
        delegate.onPause();
        return null;
    }
}
