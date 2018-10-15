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
package ru.surfstudio.android.mvp.widget.delegate;


import android.view.View;

import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * Реализация делегат для widget вью по умолчанию
 * управляет ключевыми сущностями внутренней логики виджета:
 * - PersistentScope
 * - ScreenEventDelegateManager - соответствует менеджеру экрана-контейнера
 * - ScreenState - соответствует ScreenState экрана-контейнера
 * - ScreenConfigurator
 */
public class DefaultWidgetViewDelegate implements WidgetViewDelegate {

    protected CoreWidgetViewInterface coreWidgetView;
    protected PersistentScopeStorage scopeStorage;
    protected ParentPersistentScopeFinder parentPersistentScopeFinder;
    protected String parentScopeId; // id родительского скоупа (необходим для получения уникального имени виджета)
    private View widget;

    public <W extends View & CoreWidgetViewInterface> DefaultWidgetViewDelegate(W coreWidgetView,
                                                                                PersistentScopeStorage scopeStorage,
                                                                                ParentPersistentScopeFinder parentPersistentScopeFinder) {
        this.coreWidgetView = coreWidgetView;
        this.widget = coreWidgetView;
        this.scopeStorage = scopeStorage;
        this.parentPersistentScopeFinder = parentPersistentScopeFinder;
    }

    @Override
    public void onCreate() {
        initPersistentScope();
        getScreenState().onCreate(widget, coreWidgetView);
        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();
    }

    @Override
    public void onDestroy() {
        getScreenState().onDestroy();
        if (getScreenState().isCompletelyDestroyed()) {
            scopeStorage.remove(getScopeId());
        }
    }

    @Override
    public WidgetViewPersistentScope getPersistentScope() {
        return scopeStorage.get(getScopeId(), WidgetViewPersistentScope.class);
    }

    protected void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    protected void initPersistentScope() {
        ScreenPersistentScope parentScope = parentPersistentScopeFinder.find();
        if (parentScope == null) {
            throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
        }

        parentScopeId = parentScope.getScopeId();

        if (!scopeStorage.isExist(getScopeId())) {
            BaseWidgetViewConfigurator configurator = createWidgetViewConfigurator(coreWidgetView);
            WidgetViewPersistentScope persistentScope = createWidgetViewPersistentScope(parentScope, configurator);
            configurator.setPersistentScope(persistentScope);
            scopeStorage.put(persistentScope);
        }
    }

    protected WidgetViewPersistentScope createWidgetViewPersistentScope(ScreenPersistentScope parentScope, BaseWidgetViewConfigurator configurator) {
        return new WidgetViewPersistentScope(
                parentScope.getScreenEventDelegateManager(),
                createWidgetScreenState(parentScope),
                configurator,
                getScopeId());
    }

    protected WidgetScreenState createWidgetScreenState(ScreenPersistentScope parentScope) {
        return new WidgetScreenState(parentScope.getScreenState());
    }

    protected BaseWidgetViewConfigurator createWidgetViewConfigurator(CoreWidgetViewInterface coreWidgetView) {
        return coreWidgetView.createConfigurator();
    }

    protected WidgetScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    protected String getScopeId() {
        return coreWidgetView.getName() + parentScopeId;
    }
}
