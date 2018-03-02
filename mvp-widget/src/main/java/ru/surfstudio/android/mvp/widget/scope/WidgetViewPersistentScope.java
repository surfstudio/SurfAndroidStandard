package ru.surfstudio.android.mvp.widget.scope;


import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState;

/**
 * {@link PersistentScope} для WidgetView
 */
public class WidgetViewPersistentScope extends PersistentScope {

    public WidgetViewPersistentScope(ScreenEventDelegateManager parentScreenEventDelegateManager,
                                     WidgetScreenState parentScreenState,
                                     BaseWidgetViewConfigurator configurator) {
        super(parentScreenEventDelegateManager, parentScreenState, configurator);
    }

    @Override
    public BaseWidgetViewConfigurator getConfigurator() {
        return (BaseWidgetViewConfigurator) super.getConfigurator();
    }

    @Override
    public WidgetScreenState getScreenState() {
        return (WidgetScreenState) super.getScreenState();
    }
}
