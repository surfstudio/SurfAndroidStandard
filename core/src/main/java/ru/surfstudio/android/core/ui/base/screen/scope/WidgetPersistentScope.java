package ru.surfstudio.android.core.ui.base.screen.scope;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;


/**
 * {@link PersistentScope} для WidgetView
 */
public class WidgetPersistentScope extends
        PersistentScope<ScreenEventDelegateManager, ScreenState> {
    private ScreenType parentType;

    public WidgetPersistentScope(String name,
                                 ScreenEventDelegateManager parentScreenEventDelegateManager,
                                 ScreenState parentScreenState) {
        super(name, parentScreenEventDelegateManager, parentScreenState);
        this.parentType = parentScreenState instanceof ActivityScreenState
                ? ScreenType.ACTIVITY
                : ScreenType.FRAGMENT;
    }

    public ScreenType getParentType() {
        return parentType;
    }
}
