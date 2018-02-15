package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.destroy;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnDestroyEventResolver extends MultipleScreenEventResolver<OnDestroyEvent, OnDestroyDelegate, Void> {
    @Override
    public Class<OnDestroyDelegate> getDelegateType() {
        return OnDestroyDelegate.class;
    }

    @Override
    public Class<OnDestroyEvent> getEventType() {
        return OnDestroyEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnDestroyDelegate delegate, OnDestroyEvent event) {
        delegate.onDestroy();
        return null;
    }
}
