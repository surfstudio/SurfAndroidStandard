/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.mvp.widget.delegate;


import android.view.View;

import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * делегат для widget вью,
 * управляет ключевыми сущностями внутренней логики виджета:
 * - PersistentScope
 * - ScreenEventDelegateManager - соответствует менеджеру экрана-контейнера
 * - ScreenState - соответствует ScreenState экрана-контейнера
 * - ScreenConfigurator
 */
public class WidgetViewDelegate {

    private View widget;
    private CoreWidgetViewInterface coreWidgetView;
    private PersistentScopeStorage scopeStorage;
    private ParentPersistentScopeFinder parentPersistentScopeFinder;
    private String parentScopeId; // id родительского скоупа (необходим для получения уникального имени виджета)

    public <W extends View & CoreWidgetViewInterface>WidgetViewDelegate(W coreWidgetView,
                                                                        PersistentScopeStorage scopeStorage,
                                                                        ParentPersistentScopeFinder parentPersistentScopeFinder) {
        this.coreWidgetView = coreWidgetView;
        this.widget = coreWidgetView;
        this.scopeStorage = scopeStorage;
        this.parentPersistentScopeFinder = parentPersistentScopeFinder;
    }

    public void onCreate() {
        initPersistentScope();
        getScreenState().onCreate(widget, coreWidgetView);
        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();
    }

    public void onDestroy() {
        getScreenState().onDestroy();
        if (getScreenState().isCompletelyDestroyed()) {
            scopeStorage.remove(getScopeId());
        }
    }

    public WidgetViewPersistentScope getPersistentScope() {
        return scopeStorage.get(getScopeId(), WidgetViewPersistentScope.class);
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        ScreenPersistentScope parentScope = parentPersistentScopeFinder.find();
        if (parentScope == null) {
            throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
        }

        parentScopeId = parentScope.getScopeId();

        if (!scopeStorage.isExist(getScopeId())) {

            WidgetScreenState screenState = new WidgetScreenState(parentScope.getScreenState());
            BaseWidgetViewConfigurator configurator = coreWidgetView.createConfigurator();


            WidgetViewPersistentScope persistentScope = new WidgetViewPersistentScope(
                    parentScope.getScreenEventDelegateManager(),
                    screenState,
                    configurator,
                    getScopeId());
            configurator.setPersistentScope(persistentScope);
            scopeStorage.put(persistentScope);
        }
    }

    //getters

    private WidgetScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    private String getScopeId() {
        return coreWidgetView.getName() + parentScopeId;
    }
}
