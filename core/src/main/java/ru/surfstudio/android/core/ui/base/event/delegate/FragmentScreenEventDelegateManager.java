package ru.surfstudio.android.core.ui.base.event.delegate;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;

public class FragmentScreenEventDelegateManager extends BaseScreenEventDelegateManager {
    public FragmentScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers,
                                              ActivityScreenEventDelegateManager parentDelegateManager) {
        super(eventResolvers, parentDelegateManager, ScreenType.FRAGMENT);
    }
}
