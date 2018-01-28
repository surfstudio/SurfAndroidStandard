package ru.surfstudio.android.core.ui.base.screen.scope;


import ru.surfstudio.android.core.ui.base.event.delegate.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.FragmentScreenState;

public final class FragmentPersistentScope
        extends PersistentScope<FragmentScreenEventDelegateManager, FragmentScreenState> {

    public FragmentPersistentScope(
            String name,
            FragmentScreenEventDelegateManager screenEventDelegateManager,
            FragmentScreenState screenState) {
        super(name, screenEventDelegateManager, screenState);
    }
}
