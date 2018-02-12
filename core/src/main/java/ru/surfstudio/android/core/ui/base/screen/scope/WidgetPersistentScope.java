package ru.surfstudio.android.core.ui.base.screen.scope;

import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.WidgetScreenState;


/**
 * {@link PersistentScope} для WidgetView
 */
public class WidgetPersistentScope extends
        PersistentScope<ScreenEventDelegateManager, WidgetScreenState> {

    public WidgetPersistentScope(String name,
                                 ScreenEventDelegateManager parentScreenEventDelegateManager,
                                 WidgetScreenState parentScreenState) {
        super(name, parentScreenEventDelegateManager, parentScreenState);
    }
}
