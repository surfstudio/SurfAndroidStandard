package ru.surfstudio.android.core.ui.base.screen.scope;


import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityViewScreenState;

/**
 * {@link PersistentScope} для активити
 */
public final class ActivityViewPersistentScope extends ActivityPersistentScope {

    public ActivityViewPersistentScope(
            ActivityScreenEventDelegateManager screenEventDelegateManager,
            ActivityViewScreenState screenState,
            BaseActivityViewConfigurator configurator,
            String name) {
        super(screenEventDelegateManager, screenState, configurator, name);
    }

    @Override
    public BaseActivityViewConfigurator getConfigurator() {
        return (BaseActivityViewConfigurator)super.getConfigurator();
    }

    @Override
    public ActivityViewScreenState getScreenState() {
        return (ActivityViewScreenState)super.getScreenState();
    }
}
