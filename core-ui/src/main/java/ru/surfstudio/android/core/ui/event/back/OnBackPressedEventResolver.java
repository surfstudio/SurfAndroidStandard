package ru.surfstudio.android.core.ui.event.back;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnBackPressedEventResolver implements ScreenEventResolver<OnBackPressedEvent, OnBackPressedDelegate, Boolean> {
    @Override
    public Class<OnBackPressedDelegate> getDelegateType() {
        return OnBackPressedDelegate.class;
    }

    @Override
    public Class<OnBackPressedEvent> getEventType() {
        return OnBackPressedEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_TYPES;
    }

    @Override
    public Boolean resolve(List<OnBackPressedDelegate> delegates, OnBackPressedEvent event) {
        boolean result = false;
        for (OnBackPressedDelegate delegate : delegates) {
            result |= (delegate.onBackPressed());
        }
        return result;
    }
}
