package ru.surfstudio.android.core.ui.event.lifecycle.view.destroy;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
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
