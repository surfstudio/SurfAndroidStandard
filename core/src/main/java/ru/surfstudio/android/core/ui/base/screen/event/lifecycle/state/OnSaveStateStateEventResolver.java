package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnSaveStateStateEventResolver extends MultipleScreenEventResolver<OnSaveStateEvent,OnSaveStateDelegate,Void> {


    @Override
    public Class<OnSaveStateDelegate> getDelegateType() {
        return OnSaveStateDelegate.class;
    }

    @Override
    public Class<OnSaveStateEvent> getEventType() {
        return OnSaveStateEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }


    @Override
    protected Void resolve(OnSaveStateDelegate delegate, OnSaveStateEvent event) {
        delegate.onSaveState(event.getOutState());
        return null;
    }
}
