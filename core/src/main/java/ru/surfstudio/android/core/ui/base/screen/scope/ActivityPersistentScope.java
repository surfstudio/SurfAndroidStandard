package ru.surfstudio.android.core.ui.base.screen.scope;


import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;

/**
 * {@link PersistentScope} для активити
 */
public class ActivityPersistentScope extends PersistentScope {

    public ActivityPersistentScope(
            ActivityScreenEventDelegateManager screenEventDelegateManager,
            ActivityScreenState screenState,
            BaseActivityConfigurator configurator,
            String name) {
        super(screenEventDelegateManager, screenState, configurator, name);
    }

    @Override
    public BaseActivityConfigurator getConfigurator() {
        return (BaseActivityConfigurator)super.getConfigurator();
    }

    @Override
    public ActivityScreenEventDelegateManager getScreenEventDelegateManager() {
        return (ActivityScreenEventDelegateManager)super.getScreenEventDelegateManager();
    }

    @Override
    public ActivityScreenState getScreenState() {
        return (ActivityScreenState)super.getScreenState();
    }
}
