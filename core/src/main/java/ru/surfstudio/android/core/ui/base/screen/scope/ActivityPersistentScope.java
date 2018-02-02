package ru.surfstudio.android.core.ui.base.screen.scope;


import ru.surfstudio.android.core.ui.base.screen.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;

/**
 * {@link PersistentScope} для активити
 */
public final class ActivityPersistentScope
        extends PersistentScope<ActivityScreenEventDelegateManager, ActivityScreenState> {

    public ActivityPersistentScope(
            String name,
            ActivityScreenEventDelegateManager screenEventDelegateManager,
            ActivityScreenState screenState) {
        super(name, screenEventDelegateManager, screenState);
    }
}
