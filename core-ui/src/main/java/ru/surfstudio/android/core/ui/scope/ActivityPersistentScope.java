package ru.surfstudio.android.core.ui.scope;


import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;

/**
 * {@link PersistentScope} для активити
 */
public class ActivityPersistentScope extends ScreenPersistentScope {

    public ActivityPersistentScope(
            ActivityScreenEventDelegateManager screenEventDelegateManager,
            ActivityScreenState screenState,
            BaseActivityConfigurator configurator) {
        super(screenEventDelegateManager, screenState, configurator);
    }

    @Override
    public BaseActivityConfigurator getConfigurator() {
        return (BaseActivityConfigurator) super.getConfigurator();
    }

    @Override
    public ActivityScreenEventDelegateManager getScreenEventDelegateManager() {
        return (ActivityScreenEventDelegateManager) super.getScreenEventDelegateManager();
    }

    @Override
    public ActivityScreenState getScreenState() {
        return (ActivityScreenState) super.getScreenState();
    }
}
