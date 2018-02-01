package ru.surfstudio.android.core.ui.base.screen.event.newintent;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.SingleScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
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
