package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.resume;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.MultipleScreenEventResolver;

import java.util.List;

public class OnResumeEventResolver extends MultipleScreenEventResolver<OnResumeEvent, OnResumeDelegate, Void> {
    @Override
    public Class<OnResumeDelegate> getDelegateType() {
        return OnResumeDelegate.class;
    }

    @Override
    public Class<OnResumeEvent> getEventType() {
        return OnResumeEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnResumeDelegate delegate, OnResumeEvent event) {
        delegate.onResume();
        return null;
    }
}
