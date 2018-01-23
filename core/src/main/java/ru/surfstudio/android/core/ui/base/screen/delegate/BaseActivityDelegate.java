package ru.surfstudio.android.core.ui.base.screen.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.BaseScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.activity.result.ActivityResultEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.back.OnBackPressedEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
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
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.scope.activity.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;

/**
 * делегат для любой активити, создает и управляет @PerActivity scope
 */
public class BaseActivityDelegate {

    private BaseActivityInterface baseActivity;
    private List<ScreenEventResolver> eventResolvers;
    private FragmentActivity activity;
    private BaseActivityConfigurator activityConfigurator;
    private PersistentScopeStorage scopeStorage;

    public <A extends FragmentActivity & BaseActivityInterface> BaseActivityDelegate(
            A activity,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers) {
        this.activity = activity;
        this.baseActivity = activity;
        this.eventResolvers = eventResolvers;
        this.scopeStorage = scopeStorage;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        initConfigurator();
        initPersistentScope();
        runConfigurators();
        getEventDelegateManager().sendEvent(new OnCreateEvent());
        getEventDelegateManager().sendEvent(new OnCreateActivityEvent(activity));
        getEventDelegateManager().sendEvent(new OnRestoreStateEvent(savedInstanceState));
    }

    private void initConfigurator() {
        activityConfigurator = createConfigurator();
    }

    protected void runConfigurators() {
        activityConfigurator.run();
    }

    private void initPersistentScope() {
        if (getPersistentScope() == null) {
            ActivityPersistentScope activityScope = new ActivityPersistentScope(
                    new ActivityScreenEventDelegateManager(eventResolvers));
            scopeStorage.putActivityScope(activityScope);
        }
    }

    protected BaseActivityConfigurator createConfigurator() {
        return baseActivity.createActivityConfigurator();
    }

    public BaseActivityConfigurator getConfigurator() {
        return activityConfigurator;
    }

    protected BaseScreenEventDelegateManager getEventDelegateManager() {
        return getPersistentScope().getScreenEventDelegateManager();
    }

    public String getName() {
        return activityConfigurator.getName();
    }

    public ActivityPersistentScope getPersistentScope() {
        return scopeStorage.getActivityScope();
    } //todo delete

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
