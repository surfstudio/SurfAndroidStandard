package ru.surfstudio.android.core.ui.base.screen.event;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;


/**
 * {@link ScreenEventDelegateManager} для активити
 */
public class ActivityScreenEventDelegateManager extends BaseScreenEventDelegateManager {
    public ActivityScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers) {
        super(eventResolvers, null, ScreenType.ACTIVITY);
    }
}
