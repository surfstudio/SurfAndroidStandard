package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.completely.destroy;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnCompletelyDestroyEventResolver extends MultipleScreenEventResolver<OnCompletelyDestroyEvent, OnCompletelyDestroyDelegate, Void> {
    @Override
    public Class<OnCompletelyDestroyDelegate> getDelegateType() {
        return OnCompletelyDestroyDelegate.class;
    }

    @Override
    public Class<OnCompletelyDestroyEvent> getEventType() {
        return OnCompletelyDestroyEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnCompletelyDestroyDelegate delegate, OnCompletelyDestroyEvent event) {
        delegate.onCompletelyDestroy();
        return null;
    }
}
