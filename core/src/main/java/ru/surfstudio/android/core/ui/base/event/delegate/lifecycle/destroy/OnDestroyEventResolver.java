package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

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
