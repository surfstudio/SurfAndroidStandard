package ru.surfstudio.android.core.ui.base.screen.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.event.delegate.BaseScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.activity.result.ActivityResultEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.back.OnBackPressedEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.activity.OnCreateActivityEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.common.OnCreateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy.OnDestroyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.pause.OnPauseEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.resume.OnResumeEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.start.OnStartEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnRestoreStateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnSaveStateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.stop.OnStopEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.view.destroy.OnViewDestroyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.newintent.NewIntentEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.permission.result.RequestPermissionsResultEvent;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeManager;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeManagerInitializer;
import ru.surfstudio.android.core.ui.base.scope.activity.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;

/**
 * делегат для любой активити, создает и управляет @PerActivity scope
 */
public class BaseActivityDelegate {

    private BaseActivityInterface baseActivity;
    private FragmentActivity activity;
    private BaseActivityConfigurator baseActivityConfigurator;
    private PersistentScopeManager scopeManager;

    public <A extends FragmentActivity & BaseActivityInterface> BaseActivityDelegate(A activity) {
        this.activity = activity;
        this.baseActivity = activity;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        initScopeManager();
        createConfigurators();
        initPersistentScope();
        runConfigurators();
        getEventDelegateManager().sendEvent(new OnCreateEvent());
        getEventDelegateManager().sendEvent(new OnCreateActivityEvent(activity));
        getEventDelegateManager().sendEvent(new OnRestoreStateEvent(savedInstanceState));
    }

    private void initScopeManager() {
        PersistentScopeManagerInitializer scopeManagerInitializer = createPersistentScopeManagerInitializer();
        scopeManager = scopeManagerInitializer.init(activity);
    }

    protected void runConfigurators() {
        baseActivityConfigurator.init(getPersistentScope());
    }

    private void initPersistentScope() {
        if (getPersistentScope() == null) {
            scopeManager.createActivityScope(getName());
        }
    }

    protected void createConfigurators() {
        baseActivityConfigurator = baseActivity.createActivityConfigurator();
    }

    private PersistentScopeManagerInitializer createPersistentScopeManagerInitializer() {
        return new PersistentScopeManagerInitializer();
    }

    public BaseActivityConfigurator getBaseActivityConfigurator() {
        return baseActivityConfigurator;
    }

    protected BaseScreenEventDelegateManager getEventDelegateManager() {
        return getPersistentScope().getScreenEventDelegateManager();
    }

    public String getName() {
        return baseActivityConfigurator.getName();
    }

    public ActivityPersistentScope getPersistentScope() {
        return (ActivityPersistentScope) scopeManager.getScope(getName());
    }

    public void onStart() {
        getEventDelegateManager().sendEvent(new OnStartEvent());
    }

    public void onResume() {
        getEventDelegateManager().sendEvent(new OnResumeEvent());
    }

    public void onPause() {
        getEventDelegateManager().sendEvent(new OnPauseEvent());
    }

    public void onStop() {
        getEventDelegateManager().sendEvent(new OnStopEvent());
    }

    public boolean onBackPressed() {
        return getEventDelegateManager().sendEvent(new OnBackPressedEvent());
    }

    public void onOnSaveInstantState(Bundle outState) {
        getEventDelegateManager().sendEvent(new OnSaveStateEvent(outState));
    }

    public void onDestroy() {
        getEventDelegateManager().sendEvent(new OnViewDestroyEvent());
        getEventDelegateManager().sendEvent(new OnDestroyEvent());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getEventDelegateManager().sendEvent(new ActivityResultEvent(requestCode, resultCode, data));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getEventDelegateManager().sendEvent(new RequestPermissionsResultEvent(requestCode, permissions, grantResults));
    }

    public void onNewIntent(Intent intent) {
        getEventDelegateManager().sendEvent(new NewIntentEvent(intent));
    }


}
