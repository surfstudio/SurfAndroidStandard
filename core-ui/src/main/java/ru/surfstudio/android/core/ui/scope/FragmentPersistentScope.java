package ru.surfstudio.android.core.ui.scope;


import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.event.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.state.FragmentScreenState;


/**
 * {@link PersistentScope} для фрагмента
 */
public class FragmentPersistentScope extends PersistentScope {

    public FragmentPersistentScope(
            FragmentScreenEventDelegateManager screenEventDelegateManager,
            FragmentScreenState screenState,
            BaseFragmentConfigurator configurator) {
        super(screenEventDelegateManager, screenState, configurator);
    }

    @Override
    public BaseFragmentConfigurator getConfigurator() {
        return (BaseFragmentConfigurator) super.getConfigurator();
    }

    @Override
    public FragmentScreenEventDelegateManager getScreenEventDelegateManager() {
        return (FragmentScreenEventDelegateManager) super.getScreenEventDelegateManager();
    }

    @Override
    public FragmentScreenState getScreenState() {
        return (FragmentScreenState) super.getScreenState();
    }
}
