package ru.surfstudio.android.core.ui.base.screen.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.event.delegate.BaseScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.activity.result.ActivityResultEvent;
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
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeManager;
import ru.surfstudio.android.core.ui.base.scope.activity.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.BaseFragmentInterface;

/**
 * делегат для любой активити, создает и управляет @PerActivity scope

 */
public class BaseFragmentDelegate {

    private Fragment fragment;
    private PersistentScopeManager scopeManager;
    private BaseFragmentConfigurator baseFragmentConfigurator;

    public <F extends Fragment & BaseFragmentInterface> BaseFragmentDelegate(F baseFragment) {
        this.fragment = baseFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        initScopeManager();
        createConfigurators();
        initPersistentScope();
        runConfigurators();
        getEventDelegateManager().sendEvent(new OnCreateEvent());
        getEventDelegateManager().sendEvent(new OnCreateFragmentEvent(fragment));
        getEventDelegateManager().sendEvent(new OnRestoreStateEvent(savedInstanceState));
    }

    private void initScopeManager() {
        scopeManager = PersistentScopeManager.get(fragment.getActivity());
    }

    protected void runConfigurators() {
        //empty
    }

    private void initPersistentScope() {
        if(getPersistentScope() == null){
            scopeManager.createFragmentScope(getName());
        }
    }

    protected void createConfigurators() {
        baseFragmentConfigurator = new BaseFragmentConfigurator(fragment);
    }

    protected BaseScreenEventDelegateManager getEventDelegateManager(){
        return getPersistentScope().getScreenEventDelegateManager();
    }

    public String getName(){
        return baseFragmentConfigurator.getName();
    }

    public ActivityPersistentScope getPersistentScope(){
        return (ActivityPersistentScope)scopeManager.getScope(getName());
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

    public void onOnSaveInstantState(Bundle outState){
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
