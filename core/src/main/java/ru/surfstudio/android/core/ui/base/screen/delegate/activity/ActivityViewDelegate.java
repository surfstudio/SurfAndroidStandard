package ru.surfstudio.android.core.ui.base.screen.delegate.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityViewScreenState;

/**
 * делегат для активити вью, кроме логики базового делегата добавляет управление предентерами
 */
public class ActivityViewDelegate extends ActivityDelegate {

    private CoreActivityViewInterface coreActivityView;
    private FragmentActivity activity;

    public <A extends FragmentActivity & CoreActivityViewInterface> ActivityViewDelegate(
            A activity,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            ActivityCompletelyDestroyChecker completelyDestroyChecker) {
        super(activity, scopeStorage, eventResolvers, completelyDestroyChecker);
        this.coreActivityView = activity;
        this.activity = activity;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle) {
        coreActivityView.bindPresenters();
        super.prepareView(savedInstanceState, persistableBundle);
    }

    @Override
    protected void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState) {
        getScreenState().onCreate(activity, coreActivityView, savedInstanceState);
    }

    @NonNull
    @Override
    protected ActivityViewPersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers) {
        ActivityScreenEventDelegateManager eventDelegateManager =
                new ActivityScreenEventDelegateManager(eventResolvers);
        ActivityViewScreenState screenState = new ActivityViewScreenState();
        BaseActivityViewConfigurator configurator = coreActivityView.createConfigurator();
        ActivityViewPersistentScope persistentScope = new ActivityViewPersistentScope(
                eventDelegateManager,
                screenState,
                configurator,
                coreActivityView.getName());
        configurator.setPersistentScope(persistentScope);
        return persistentScope;
    }

    @Override
    public ActivityViewPersistentScope getPersistentScope() {
        return (ActivityViewPersistentScope)super.getPersistentScope();
    }

    @Override
    public ActivityViewScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }
}
