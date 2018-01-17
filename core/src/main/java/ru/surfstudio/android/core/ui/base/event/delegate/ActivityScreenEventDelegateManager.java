package ru.surfstudio.android.core.ui.base.event.delegate;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;

public class ActivityScreenEventDelegateManager extends BaseScreenEventDelegateManager {
    public ActivityScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers) {
        super(eventResolvers, null, ScreenType.ACTIVITY);
    }


}
