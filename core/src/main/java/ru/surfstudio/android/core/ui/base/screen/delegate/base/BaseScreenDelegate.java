package ru.surfstudio.android.core.ui.base.screen.delegate.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.surfstudio.android.core.ui.base.screen.configurator.Configurator;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.completely.destroy.OnCompletelyDestroyEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.destroy.OnDestroyEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.pause.OnPauseEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.ready.OnViewReadyEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.resume.OnResumeEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.start.OnStartEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state.OnRestoreStateEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state.OnSaveStateEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.stop.OnStopEvent;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.view.destroy.OnViewDestroyEvent;
import ru.surfstudio.android.core.ui.base.screen.event.result.ActivityResultEvent;
import ru.surfstudio.android.core.ui.base.screen.event.result.RequestPermissionsResultEvent;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.state.BaseScreenState;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.Logger;

/**
 * делегат для базовых активити и фрагмента,
 * управляет ключевыми сущностями внутренней логики экрана:
 * - PersistentScope
 * - ScreenEventDelegateManager
 * - ScreenState
 * - ScreenConfigurator
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

    protected abstract void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle);

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

    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle) {
        initConfigurator();
        initPersistentScope();
        notifyScreenStateAboutOnCreate(savedInstanceState);
        runConfigurator();
        prepareView(savedInstanceState, persistableBundle);
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
        Logger.d(LogConstants.LOG_SCREEN_RESUME_FORMAT, getName());
        getEventDelegateManager().sendEvent(new OnResumeEvent());
    }

    public void onPause() {
        Logger.d(LogConstants.LOG_SCREEN_PAUSE_FORMAT, getName());
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
