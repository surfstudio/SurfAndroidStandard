package ru.surfstudio.android.core.ui.event.result;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.SingleScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class ActivityResultEventResolver extends SingleScreenEventResolver<ActivityResultEvent, ActivityResultDelegate> {
    @Override
    public Class<ActivityResultDelegate> getDelegateType() {
        return ActivityResultDelegate.class;
    }

    @Override
    public Class<ActivityResultEvent> getEventType() {
        return ActivityResultEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected boolean resolve(ActivityResultDelegate delegate, ActivityResultEvent event) {
        return delegate.onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }
}
