/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.delegate.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.UUID;

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyEvent;
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
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.core.ui.state.BaseScreenState;

/**
 * базовый делегат для базовых активити и фрагмента, для виджета свой делегат
 * управляет ключевыми сущностями внутренней логики экрана:
 * - PersistentScope            - хранилище для всех остальных обьектов, переживает смену конфигурации
 * - ScreenEventDelegateManager - позволяет подписываться на события экрана
 * - ScreenState                - хранит текущее состояние экрана
 * - ScreenConfigurator         - управляет компонентами даггера, предоставляет уникальное имя экрана
 */

public abstract class BaseScreenDelegate {

    private static final String KEY_PSS_ID = "KEY_PSS_ID";

    private String currentScopeId;
    private List<ScreenEventResolver> eventResolvers;
    private PersistentScopeStorage scopeStorage;
    private CompletelyDestroyChecker completelyDestroyChecker;

    public abstract ScreenPersistentScope getPersistentScope();

    protected abstract void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState);

    protected abstract void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle);

    protected abstract PersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers);

    protected abstract BaseScreenState getScreenState();
    //core logic

    public BaseScreenDelegate(
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            CompletelyDestroyChecker completelyDestroyChecker) {
        this.eventResolvers = eventResolvers;
        this.scopeStorage = scopeStorage;
        this.completelyDestroyChecker = completelyDestroyChecker;
    }

    public void initialize(@Nullable Bundle savedInstanceState) {
        currentScopeId = savedInstanceState != null
                ? savedInstanceState.getString(KEY_PSS_ID)
                : UUID.randomUUID().toString();
        initPersistentScope();
    }

    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle) {
        notifyScreenStateAboutOnCreate(savedInstanceState);
        runConfigurator();
        prepareView(savedInstanceState, persistableBundle);
        getEventDelegateManager().sendEvent(new OnRestoreStateEvent(savedInstanceState));

        getScreenState().onViewReady();
        getEventDelegateManager().sendEvent(new OnViewReadyEvent());
    }

    public void onDestroyView() {
        getScreenState().onDestroyView();
        getEventDelegateManager().sendEvent(new OnViewDestroyEvent());
    }

    public void onDestroy() {
        if (isPersistentScopeExist() && completelyDestroyChecker.check()) {
            //onDestroy can be called without onActivityCreated for fragment,
            //so screen scope hasn't created and initialized yet
            getScreenState().onCompletelyDestroy();
            getEventDelegateManager().sendEvent(new OnCompletelyDestroyEvent());
            getEventDelegateManager().destroy();
            scopeStorage.remove(getScopeId());
        }
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        if (!isPersistentScopeExist()) {
            PersistentScope persistentScope = createPersistentScope(eventResolvers);
            scopeStorage.put(persistentScope);
        }
    }

    //getters

    protected ScreenEventDelegateManager getEventDelegateManager() {
        return getPersistentScope().getScreenEventDelegateManager();
    }

    protected String getScopeId() {
        return currentScopeId;
    }

    //other events

    public void onStart() {
        getScreenState().onStart();
        getEventDelegateManager().sendEvent(new OnStartEvent());
    }

    public void onResume() {
        getScreenState().onResume();
        getEventDelegateManager().sendEvent(new OnResumeEvent());
    }

    public void onPause() {
        getScreenState().onPause();
        getEventDelegateManager().sendEvent(new OnPauseEvent());
    }

    public void onStop() {
        getScreenState().onStop();
        getEventDelegateManager().sendEvent(new OnStopEvent());
    }

    public void onOnSaveInstantState(Bundle outState) {
        outState.putString(KEY_PSS_ID, currentScopeId);
        if (isPersistentScopeExist()) {
            //onSaveInstantState can be called without onActivityCreated for fragment,
            //so screen scope hasn't created and initialized yet
            getEventDelegateManager().sendEvent(new OnSaveStateEvent(outState));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getEventDelegateManager().sendEvent(new ActivityResultEvent(requestCode, resultCode, data));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getEventDelegateManager().sendEvent(new RequestPermissionsResultEvent(requestCode, permissions, grantResults));
    }

    private boolean isPersistentScopeExist() {
        return scopeStorage.isExist(getScopeId());
    }
}
