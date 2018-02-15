package ru.surfstudio.android.core.ui.base.screen.event;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;

/**
 * для фрагмента
 */
public class FragmentScreenEventDelegateManager extends BaseScreenEventDelegateManager {
    public FragmentScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers,
                                              ActivityScreenEventDelegateManager parentDelegateManager) {
        super(eventResolvers, parentDelegateManager, ScreenType.FRAGMENT);
    }
}
