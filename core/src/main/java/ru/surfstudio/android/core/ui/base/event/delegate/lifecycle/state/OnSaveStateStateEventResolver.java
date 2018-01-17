package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

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
