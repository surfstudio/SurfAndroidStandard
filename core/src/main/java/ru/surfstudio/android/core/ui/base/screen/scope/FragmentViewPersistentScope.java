package ru.surfstudio.android.core.ui.base.screen.scope;


import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.event.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.FragmentScreenState;
import ru.surfstudio.android.core.ui.base.screen.state.FragmentViewScreenState;


/**
 * {@link PersistentScope} для фрагмента
 */
public final class FragmentViewPersistentScope extends FragmentPersistentScope {

    public FragmentViewPersistentScope(
            FragmentScreenEventDelegateManager screenEventDelegateManager,
            FragmentScreenState screenState,
            BaseFragmentViewConfigurator configurator,
            String name) {
        super(screenEventDelegateManager, screenState, configurator, name);
    }

    @Override
    public BaseFragmentViewConfigurator getConfigurator() {
        return (BaseFragmentViewConfigurator)super.getConfigurator();
    }

    @Override
    public FragmentViewScreenState getScreenState() {
        return (FragmentViewScreenState)super.getScreenState();
    }
}
