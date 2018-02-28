package ru.surfstudio.android.core.mvp.delegate;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.mvp.state.FragmentViewScreenState;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.event.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.state.FragmentScreenState;

/**
 * делегат для фрагмент вью, кроме логики базового делегата добавляет управление предентерами
 */
public class FragmentViewDelegate extends FragmentDelegate {

    private CoreFragmentViewInterface coreFragmentView;
    private Fragment fragment;

    public <F extends Fragment & CoreFragmentViewInterface> FragmentViewDelegate(
            F fragment,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            FragmentCompletelyDestroyChecker completelyDestroyChecker) {
        super(fragment, scopeStorage, eventResolvers, completelyDestroyChecker);
        this.coreFragmentView = fragment;
        this.fragment = fragment;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle ignore) {
        coreFragmentView.bindPresenters();
        super.prepareView(savedInstanceState, ignore);
    }

    @Override
    protected void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState) {
        this.getScreenState().onCreate(fragment, coreFragmentView, savedInstanceState);
    }

    @NonNull
    @Override
    protected FragmentViewPersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers) {
        FragmentScreenEventDelegateManager eventDelegateManager = createFragmentScreenEventDelegateManager(eventResolvers);
        FragmentViewScreenState screenState = new FragmentViewScreenState();
        BaseFragmentViewConfigurator configurator = coreFragmentView.createConfigurator();
        FragmentViewPersistentScope persistentScope = new FragmentViewPersistentScope(
                eventDelegateManager,
                screenState,
                configurator,
                coreFragmentView.getName());
        configurator.setPersistentScope(persistentScope);
        return persistentScope;
    }

    @Override
    public FragmentViewPersistentScope getPersistentScope() {
        return (FragmentViewPersistentScope) super.getPersistentScope();
    }

    @Override
    public FragmentViewScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }
}
