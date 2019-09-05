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
package ru.surfstudio.android.core.mvp.delegate;


import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.mvp.state.ActivityViewScreenState;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.event.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;

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
                getScopeId());
        configurator.setPersistentScope(persistentScope);
        return persistentScope;
    }

    @Override
    public ActivityViewPersistentScope getPersistentScope() {
        return (ActivityViewPersistentScope) super.getPersistentScope();
    }

    @Override
    public ActivityViewScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }
}
