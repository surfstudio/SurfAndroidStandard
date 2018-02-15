package ru.surfstudio.android.core.ui.base.screen.scope;


import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.base.screen.event.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.FragmentScreenState;


/**
 * {@link PersistentScope} для фрагмента
 */
public class FragmentPersistentScope extends PersistentScope {

    public FragmentPersistentScope(
            FragmentScreenEventDelegateManager screenEventDelegateManager,
            FragmentScreenState screenState,
            BaseFragmentConfigurator configurator,
            String name) {
        super(screenEventDelegateManager, screenState, configurator, name);
    }

    @Override
    public BaseFragmentConfigurator getConfigurator() {
        return (BaseFragmentConfigurator)super.getConfigurator();
    }

    @Override
    public FragmentScreenEventDelegateManager getScreenEventDelegateManager() {
        return (FragmentScreenEventDelegateManager)super.getScreenEventDelegateManager();
    }

    @Override
    public FragmentScreenState getScreenState() {
        return (FragmentScreenState) super.getScreenState();
    }
}
