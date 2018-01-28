package ru.surfstudio.android.core.ui.base.screen.delegate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.base.BaseScreenDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.state.FragmentScreenState;

/**
 * делегат для любой активити, создает и управляет @PerActivity scope

 */
public class FragmentDelegate extends BaseScreenDelegate<
        FragmentPersistentScope,
        FragmentScreenState,
        BaseFragmentConfigurator,
        FragmentCompletelyDestroyChecker> {

    private Fragment fragment;
    private CoreFragmentInterface coreFragment;
    private PersistentScopeStorage scopeStorage;

    public <F extends Fragment & CoreFragmentInterface> FragmentDelegate(
            F fragment,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            FragmentCompletelyDestroyChecker completelyDestroyChecker) {
        super(scopeStorage, eventResolvers, completelyDestroyChecker);
        this.fragment = fragment;
        this.coreFragment = fragment;
        this.scopeStorage = scopeStorage;
    }

    @Override
    protected void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState) {
        getScreenState().onCreate(fragment, savedInstanceState);
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState) {
        coreFragment.onActivityCreated(savedInstanceState, getScreenState().isViewRecreated());
    }

    @Override
    protected BaseFragmentConfigurator createConfigurator() {
        return coreFragment.createConfigurator();
    }

    @NonNull
    @Override
    protected FragmentPersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers) {
        ActivityPersistentScope activityPersistentScope = scopeStorage.getActivityScope();
        if (activityPersistentScope == null) {
            throw new IllegalStateException("FragmentPersistentScope cannot be created without ActivityPersistentScope");
        }
        FragmentScreenEventDelegateManager eventDelegateManager = new FragmentScreenEventDelegateManager(
                eventResolvers,
                activityPersistentScope.getScreenEventDelegateManager());
        FragmentScreenState screenState = new FragmentScreenState();
        return new FragmentPersistentScope(
                getName(),
                eventDelegateManager,
                screenState);
    }

    @Override
    protected FragmentPersistentScope getPersistentScope() {
        return scopeStorage.getFragmentScope(getName());
    }
}
