package ru.surfstudio.android.core.mvp.scope;


import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.mvp.state.FragmentViewScreenState;
import ru.surfstudio.android.core.ui.event.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.state.FragmentScreenState;

/**
 * {@link PersistentScope} для фрагмента
 */
public final class FragmentViewPersistentScope extends FragmentPersistentScope {

    public FragmentViewPersistentScope(
            FragmentScreenEventDelegateManager screenEventDelegateManager,
            FragmentScreenState screenState,
            BaseFragmentViewConfigurator configurator,
            String scopeId) {
        super(screenEventDelegateManager, screenState, configurator, scopeId);
    }

    @Override
    public BaseFragmentViewConfigurator getConfigurator() {
        return (BaseFragmentViewConfigurator) super.getConfigurator();
    }

    @Override
    public FragmentViewScreenState getScreenState() {
        return (FragmentViewScreenState) super.getScreenState();
    }
}
