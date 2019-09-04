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
package ru.surfstudio.android.core.ui.delegate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.delegate.base.BaseScreenDelegate;
import ru.surfstudio.android.core.ui.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.back.OnBackPressedEvent;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.newintent.NewIntentEvent;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;

/**
 * делегат для базовой активити,
 * управляет ключевыми сущностями внутренней логики экрана:
 * - PersistentScope            - хранилище для всех остальных обьектов, переживает смену конфигурации
 * - ScreenEventDelegateManager - позволяет подписываться на события экрана
 * - ScreenState                - хранит текущее состояние экрана
 * - ScreenConfigurator         - управляет компонентами даггера, предоставляет уникальное имя экрана
 */
public class ActivityDelegate extends BaseScreenDelegate {

    private FragmentActivity activity;
    private CoreActivityInterface coreActivity;
    private final PersistentScopeStorage scopeStorage;

    public <A extends FragmentActivity & CoreActivityInterface> ActivityDelegate(
            A activity,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            ActivityCompletelyDestroyChecker completelyDestroyChecker) {
        super(scopeStorage, eventResolvers, completelyDestroyChecker);
        this.activity = activity;
        this.coreActivity = activity;
        this.scopeStorage = scopeStorage;
    }

    @Override
    public ActivityPersistentScope getPersistentScope() {
        return scopeStorage.get(getScopeId(), ActivityPersistentScope.class);
    }

    @Override
    public ActivityScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    @Override
    protected void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState) {
        this.getScreenState().onCreate(activity, coreActivity, savedInstanceState);
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle) {
        coreActivity.onCreate(savedInstanceState, persistableBundle, getScreenState().isViewRecreated());
    }

    @NonNull
    @Override
    protected ActivityPersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers) {
        ActivityScreenEventDelegateManager eventDelegateManager =
                new ActivityScreenEventDelegateManager(eventResolvers);
        ActivityScreenState screenState = new ActivityScreenState();
        BaseActivityConfigurator configurator = coreActivity.createConfigurator();
        ActivityPersistentScope persistentScope = new ActivityPersistentScope(
                eventDelegateManager,
                screenState,
                configurator,
                getScopeId());
        configurator.setPersistentScope(persistentScope);
        return persistentScope;
    }

    //activity specific events

    public void onNewIntent(Intent intent) {
        getEventDelegateManager().sendEvent(new NewIntentEvent(intent));
    }

    public boolean onBackPressed() {
        return getEventDelegateManager().sendEvent(new OnBackPressedEvent());
    }
}
