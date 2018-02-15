package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.resume;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
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
