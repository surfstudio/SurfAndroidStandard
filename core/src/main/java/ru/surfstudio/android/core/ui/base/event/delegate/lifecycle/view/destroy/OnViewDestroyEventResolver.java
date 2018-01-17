package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.view.destroy;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

public class OnViewDestroyEventResolver extends MultipleScreenEventResolver<OnViewDestroyEvent, OnViewDestroyDelegate, Void> {
    @Override
    public Class<OnViewDestroyDelegate> getDelegateType() {
        return OnViewDestroyDelegate.class;
    }

    @Override
    public Class<OnViewDestroyEvent> getEventType() {
        return OnViewDestroyEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnViewDestroyDelegate delegate, OnViewDestroyEvent event) {
        delegate.onViewDestroy();
        return null;
    }
}
