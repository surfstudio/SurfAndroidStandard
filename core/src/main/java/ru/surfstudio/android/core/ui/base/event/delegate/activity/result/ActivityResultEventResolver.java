package ru.surfstudio.android.core.ui.base.event.delegate.activity.result;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.SingleScreenEventResolver;

import java.util.List;

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
