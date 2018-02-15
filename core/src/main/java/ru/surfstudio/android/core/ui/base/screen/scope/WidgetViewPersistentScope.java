package ru.surfstudio.android.core.ui.base.screen.scope;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.WidgetScreenState;


/**
 * {@link PersistentScope} для WidgetView
 */
public class WidgetViewPersistentScope extends PersistentScope {

    public WidgetViewPersistentScope(ScreenEventDelegateManager parentScreenEventDelegateManager,
                                     WidgetScreenState parentScreenState,
                                     BaseWidgetViewConfigurator configurator,
                                     String name) {
        super(parentScreenEventDelegateManager, parentScreenState, configurator, name);
    }

    @Override
    public BaseWidgetViewConfigurator getConfigurator() {
        return (BaseWidgetViewConfigurator)super.getConfigurator();
    }

    @Override
    public WidgetScreenState getScreenState() {
        return (WidgetScreenState)super.getScreenState();
    }
}
