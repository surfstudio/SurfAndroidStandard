package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.fragment;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

public class OnCreateFragmentEventResolver extends MultipleScreenEventResolver<OnCreateFragmentEvent, OnCreateFragmentDelegate, Void> {
    @Override
    public Class<OnCreateFragmentDelegate> getDelegateType() {
        return OnCreateFragmentDelegate.class;
    }

    @Override
    public Class<OnCreateFragmentEvent> getEventType() {
        return OnCreateFragmentEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnCreateFragmentDelegate delegate, OnCreateFragmentEvent event) {
        delegate.onCreate(event.getFragment());
        return null;
    }
}
