package ru.surfstudio.android.core.ui.delegate.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.destroy.OnDestroyEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyEvent;
import ru.surfstudio.android.core.ui.event.result.ActivityResultEvent;
import ru.surfstudio.android.core.ui.event.result.RequestPermissionsResultEvent;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.state.BaseScreenState;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.Logger;

/**
 * базовый делегат для базовых активити и фрагмента, для виджета свой делегат
 * управляет ключевыми сущностями внутренней логики экрана:
 * - PersistentScope            - хранилище для всех остальных обьектов, переживает смену конфигурации
 * - ScreenEventDelegateManager - позволяет подписываться на события экрана
 * - ScreenState                - хранит текущее состояние экрана
 * - ScreenConfigurator         - управляет компонентами даггера, предоставляет уникальное имя экрана
 */

public abstract class BaseScreenDelegate {

    private HasName screenNameProvider;
    private List<ScreenEventResolver> eventResolvers;
    private PersistentScopeStorage scopeStorage;
    private CompletelyDestroyChecker completelyDestroyChecker;

    public abstract PersistentScope getPersistentScope();

    protected abstract void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState);

    protected abstract void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle);

    protected abstract PersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers);

    protected abstract BaseScreenState getScreenState();
    //core logic

    public BaseScreenDelegate(
            HasName screenNameProvider,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            CompletelyDestroyChecker completelyDestroyChecker) {
        this.screenNameProvider = screenNameProvider;
        this.eventResolvers = eventResolvers;
        this.scopeStorage = scopeStorage;
        this.completelyDestroyChecker = completelyDestroyChecker;
    }

    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle) {
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
            scopeStorage.remove(getName());
        }
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        if (!scopeStorage.isExist(getName())) {
            PersistentScope persistentScope = createPersistentScope(eventResolvers);
            scopeStorage.put(persistentScope);
        }
    }

    //getters

    protected ScreenEventDelegateManager getEventDelegateManager() {
        return getPersistentScope().getScreenEventDelegateManager();
    }

    protected String getName() {
        return screenNameProvider.getName();
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
