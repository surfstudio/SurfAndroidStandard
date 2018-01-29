package ru.surfstudio.android.core.ui.base.screen.delegate.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.activity.result.ActivityResultEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.completely.destroy.OnCompletelyDestroyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy.OnDestroyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.pause.OnPauseEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready.OnViewReadyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.resume.OnResumeEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.start.OnStartEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnRestoreStateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnSaveStateEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.stop.OnStopEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.view.destroy.OnViewDestroyEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.permission.result.RequestPermissionsResultEvent;
import ru.surfstudio.android.core.ui.base.screen.configurator.Configurator;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.state.BaseScreenState;

/**
 * Created by makstuev on 27.01.2018.
 */

public abstract class BaseScreenDelegate<
        P extends PersistentScope<? extends ScreenEventDelegateManager, S>,
        S extends BaseScreenState,
        C extends Configurator,
        D extends CompletelyDestroyChecker> {

    private List<ScreenEventResolver> eventResolvers;
    private C configurator;
    private PersistentScopeStorage scopeStorage;
    private D completelyDestroyChecker;

    protected abstract void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState);

    protected abstract void prepareView(@Nullable Bundle savedInstanceState);

    protected abstract P createPersistentScope(List<ScreenEventResolver> eventResolvers);

    protected abstract P getPersistentScope();

    protected abstract C createConfigurator();

    //core logic

    public BaseScreenDelegate(
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            D completelyDestroyChecker) {
        this.eventResolvers = eventResolvers;
        this.scopeStorage = scopeStorage;
        this.completelyDestroyChecker = completelyDestroyChecker;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        initConfigurator();
        initPersistentScope();
        notifyScreenStateAboutOnCreate(savedInstanceState);
        runConfigurator();
        prepareView(savedInstanceState);
        getEventDelegateManager().sendEvent(new OnRestoreStateEvent(savedInstanceState));
        getEventDelegateManager().sendEvent(new OnViewReadyEvent());
    }

    public void onDestroyView() {
        getScreenState().onDestroyView();
        getEventDelegateManager().sendEvent(new OnViewDestroyEvent());
    }

    public void onDestroy() {
        getScreenState().onDestroy();
        getEventDelegateManager().sendEvent(new OnDestroyEvent());
        if (completelyDestroyChecker.check()) {
            getScreenState().onCompletelyDestroy();
            getEventDelegateManager().sendEvent(new OnCompletelyDestroyEvent());
            getEventDelegateManager().destroy();
            scopeStorage.removeScope(getName());
        }
    }

    private void initConfigurator() {
        configurator = createConfigurator();
    }

    private void runConfigurator() {
        configurator.run();
    }

    private void initPersistentScope() {
        if (getPersistentScope() == null) {
            P persistentScope = createPersistentScope(eventResolvers);
            scopeStorage.putScope(persistentScope);
        }
        configurator.setPersistentScope(getPersistentScope());
    }

    //getters

    public C getConfigurator() {
        return configurator;
    }

    protected S getScreenState() {
        return getPersistentScope().getScreenState();
    }

    protected ScreenEventDelegateManager getEventDelegateManager() {
        return getPersistentScope().getScreenEventDelegateManager();
    }

    protected String getName() {
        return configurator.getName();
    }

    //other events

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

    public void onOnSaveInstantState(Bundle outState) {
        getEventDelegateManager().sendEvent(new OnSaveStateEvent(outState));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getEventDelegateManager().sendEvent(new ActivityResultEvent(requestCode, resultCode, data));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getEventDelegateManager().sendEvent(new RequestPermissionsResultEvent(requestCode, permissions, grantResults));
    }
}
