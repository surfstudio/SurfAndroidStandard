package ru.surfstudio.android.core.mvp.scope;


import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.mvp.state.ActivityViewScreenState;
import ru.surfstudio.android.core.ui.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScope;

/**
 * {@link PersistentScope} для активити
 */
public final class ActivityViewPersistentScope extends ActivityPersistentScope {

    public ActivityViewPersistentScope(
            ActivityScreenEventDelegateManager screenEventDelegateManager,
            ActivityViewScreenState screenState,
            BaseActivityViewConfigurator configurator,
            String scopeId) {
        super(screenEventDelegateManager, screenState, configurator, scopeId);
    }

    @Override
    public BaseActivityViewConfigurator getConfigurator() {
        return (BaseActivityViewConfigurator) super.getConfigurator();
    }

    @Override
    public ActivityViewScreenState getScreenState() {
        return (ActivityViewScreenState) super.getScreenState();
    }
}
