package ru.surfstudio.android.core.ui.base.screen.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.BaseScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.activity.result.ActivityResultEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.common.OnCreateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.fragment.OnCreateFragmentEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy.OnDestroyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.pause.OnPauseEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.resume.OnResumeEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.start.OnStartEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnRestoreStateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnSaveStateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.stop.OnStopEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.view.destroy.OnViewDestroyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.permission.result.RequestPermissionsResultEvent;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.scope.activity.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.scope.fragment.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.BaseFragmentInterface;

/**
 * делегат для любой активити, создает и управляет @PerActivity scope

 */
public class BaseFragmentDelegate {

    private Fragment fragment;
    private List<ScreenEventResolver> eventResolvers;
    private PersistentScopeStorage scopeStorage;
    private BaseFragmentConfigurator fragmentConfigurator;

    public <F extends Fragment & BaseFragmentInterface> BaseFragmentDelegate(
            F baseFragment,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers) {
        this.fragment = baseFragment;
        this.scopeStorage = scopeStorage;
        this.eventResolvers = eventResolvers;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        initConfigurator();
        initPersistentScope(); //todo set to configurator
        runConfigurator();
        getEventDelegateManager().sendEvent(new OnCreateEvent());
        getEventDelegateManager().sendEvent(new OnCreateFragmentEvent(fragment));
        getEventDelegateManager().sendEvent(new OnRestoreStateEvent(savedInstanceState));
    }

    private void initConfigurator() {
        fragmentConfigurator = createConfigurator();
    }

    protected void runConfigurator() {
        fragmentConfigurator.run();
    }

    private void initPersistentScope() {
        if (getPersistentScope() == null) {
            ActivityPersistentScope activityPersistentScope = scopeStorage.getActivityScope();
            if (activityPersistentScope == null) {
                throw new IllegalStateException("FragmentPersistentScope cannot be created without ActivityPersistentScope");
            }
            FragmentScreenEventDelegateManager eventDelegateManager = new FragmentScreenEventDelegateManager(
                    eventResolvers,
                    activityPersistentScope.getScreenEventDelegateManager());
            FragmentPersistentScope fragmentScope = new FragmentPersistentScope(
                    getName(),
                    eventDelegateManager);
            scopeStorage.putFragmentScope(fragmentScope);
        }
    }

    protected BaseFragmentConfigurator createConfigurator() {
        return new BaseFragmentConfigurator(fragment);
    }

    protected BaseScreenEventDelegateManager getEventDelegateManager(){
        return getPersistentScope().getScreenEventDelegateManager();
    }

    public String getName(){
        return fragmentConfigurator.getName();
    }

    public FragmentPersistentScope getPersistentScope() {
        return scopeStorage.getFragmentScope(getName());
    }

    public BaseFragmentConfigurator getConfigurator() {
        return fragmentConfigurator;
    }

    public void onStart(){
        getEventDelegateManager().sendEvent(new OnStartEvent());
    }

    public void onResume(){
        getEventDelegateManager().sendEvent(new OnResumeEvent());
    }

    public void onPause(){
        getEventDelegateManager().sendEvent(new OnPauseEvent());
    }

    public void onStop(){
        getEventDelegateManager().sendEvent(new OnStopEvent());
    }

    public void onOnSaveInstantState(Bundle outState) { //todo check use and other
        getEventDelegateManager().sendEvent(new OnSaveStateEvent(outState));
    }

    public void onDestroyView() {
        getEventDelegateManager().sendEvent(new OnViewDestroyEvent());
    }

    public void onDestroy() {
        getEventDelegateManager().sendEvent(new OnDestroyEvent());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getEventDelegateManager().sendEvent(new ActivityResultEvent(requestCode, resultCode, data));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getEventDelegateManager().sendEvent(new RequestPermissionsResultEvent(requestCode, permissions, grantResults));
    }
}
