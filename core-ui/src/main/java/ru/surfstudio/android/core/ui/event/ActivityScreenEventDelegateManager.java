package ru.surfstudio.android.core.ui.event;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;


/**
 * {@link ScreenEventDelegateManager} для активити
 */
public class ActivityScreenEventDelegateManager extends BaseScreenEventDelegateManager {
    public ActivityScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers) {
        super(eventResolvers, null, ScreenType.ACTIVITY);
    }
}
