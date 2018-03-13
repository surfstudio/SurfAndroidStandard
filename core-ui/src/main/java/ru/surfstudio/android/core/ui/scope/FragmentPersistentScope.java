package ru.surfstudio.android.core.ui.scope;


import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.event.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.state.FragmentScreenState;


/**
 * {@link PersistentScope} для фрагмента
 */
public class FragmentPersistentScope extends ScreenPersistentScope {

    public FragmentPersistentScope(
            FragmentScreenEventDelegateManager screenEventDelegateManager,
            FragmentScreenState screenState,
            BaseFragmentConfigurator configurator,
            String scopeId) {
        super(screenEventDelegateManager, screenState, configurator, scopeId);
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
