package ru.surfstudio.android.core.ui.base.event.delegate.newintent;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.SingleScreenEventResolver;

import java.util.List;

public class NewIntentEventResolver extends SingleScreenEventResolver<NewIntentEvent, NewIntentDelegate> {
    @Override
    public Class<NewIntentDelegate> getDelegateType() {
        return NewIntentDelegate.class;
    }

    @Override
    public Class<NewIntentEvent> getEventType() {
        return NewIntentEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_TYPES;
    }

    @Override
    protected boolean resolve(NewIntentDelegate delegate, NewIntentEvent event) {
        return delegate.onNewIntent(event.getIntent());
    }
}
