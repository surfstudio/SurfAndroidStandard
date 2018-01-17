package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.completely.destroy;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

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
