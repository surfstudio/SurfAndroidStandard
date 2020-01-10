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

import java.util.List;

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.event.StageSource;
import ru.surfstudio.android.mvp.widget.event.WidgetLifecycleManager;
import ru.surfstudio.android.mvp.widget.event.delegate.WidgetScreenEventDelegateManager;
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
 * - WidgetLifecycleManager - управляет событиями ЖЦ виджета
 */
public class WidgetViewDelegate {

    private View widget;
    private CoreWidgetViewInterface coreWidgetView;
    private PersistentScopeStorage scopeStorage;
    private ParentPersistentScopeFinder parentPersistentScopeFinder;
    private final List<ScreenEventResolver> eventResolvers;
    private boolean isViewDestroyedForcibly = false; //флаг, указывающий, что view уничтожена принудительно

    private String currentScopeId;

    public <W extends View & CoreWidgetViewInterface> WidgetViewDelegate(W coreWidgetView,
                                                                         PersistentScopeStorage scopeStorage,
                                                                         ParentPersistentScopeFinder parentPersistentScopeFinder,
                                                                         List<ScreenEventResolver> eventResolvers) {
        this.coreWidgetView = coreWidgetView;
        this.widget = coreWidgetView;
        this.scopeStorage = scopeStorage;
        this.parentPersistentScopeFinder = parentPersistentScopeFinder;
        this.eventResolvers = eventResolvers;
    }

    public void onCreate() {

        initPersistentScope();
        getLifecycleManager().onCreate(widget, coreWidgetView, this);

        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();

        getLifecycleManager().onViewReady(StageSource.DELEGATE);
    }

    public void onViewDestroy() {
        if (scopeStorage.isExist(getCurrentScopeId()) && !isViewDestroyedForcibly) {
            getLifecycleManager().onViewDestroy();
        }
    }

    //вызов происходит по срабатыванию родительского OnCompletelyDestroy
    public void onCompletelyDestroy() {
        String scopeId = getCurrentScopeId();
        if (scopeStorage.isExist(scopeId)) {
            scopeStorage.remove(scopeId);
        }
    }

    public WidgetViewPersistentScope getPersistentScope() {
        return scopeStorage.get(getCurrentScopeId(), WidgetViewPersistentScope.class);
    }

    /**
     * Принудительное полное ручное уничтожение View.
     */
    public void manualCompletelyDestroy() {
        getLifecycleManager().onCompletelyDestroy(StageSource.DELEGATE);
    }

    /**
     * Пометка View как принудительно уничтоженной.
     * <p>
     * Необходимо для того, чтобы избежать повторного вызова onDestroy на onDetachFromWindow,
     * если onDestroy уже был вызван извне (возможно при помещении виджета в RecyclerView).
     */
    public void setViewDestroyedForcibly() {
        isViewDestroyedForcibly = true;
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        ScreenPersistentScope parentScope = parentPersistentScopeFinder.find();

        if (parentScope == null) {
            throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
        }

        String widgetId = coreWidgetView.getWidgetId();
        String invalidId = Integer.toString(View.NO_ID);

        if (widgetId == null || widgetId.isEmpty() || widgetId.equals(invalidId)) {
            throw new IllegalStateException("Widget must have unique view id. Please, specify it in the layout file or override getWidgetId method.");
        } else {
            String parentScopeId = parentScope.getScopeId();
            setScopeId(widgetId + parentScopeId);
        }

        if (!scopeStorage.isExist(getCurrentScopeId())) {

            WidgetScreenState screenState = new WidgetScreenState(parentScope.getScreenState());
            BaseWidgetViewConfigurator configurator = coreWidgetView.createConfigurator();
            ScreenEventDelegateManager parentDelegateManager = parentScope.getScreenEventDelegateManager();

            WidgetScreenEventDelegateManager delegateManager = new WidgetScreenEventDelegateManager(
                    eventResolvers,
                    parentDelegateManager
            );

            WidgetLifecycleManager lifecycleManager = new WidgetLifecycleManager(
                    screenState,
                    parentScope.getScreenState(),
                    delegateManager,
                    parentDelegateManager);

            WidgetViewPersistentScope persistentScope = new WidgetViewPersistentScope(
                    delegateManager,
                    screenState,
                    configurator,
                    getCurrentScopeId(),
                    lifecycleManager);

            configurator.setPersistentScope(persistentScope);
            scopeStorage.put(persistentScope);
        }
    }

    private void setScopeId(String scopeId) {
        this.currentScopeId = scopeId;
    }

    //getters

    private WidgetScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    private String getCurrentScopeId() {
        return currentScopeId;
    }

    private WidgetLifecycleManager getLifecycleManager() {
        return getPersistentScope().getLifecycleManager();
    }

    /**
     * Коллбек, вызываемый при уничтожении View.
     * <p>
     * Следует заменить на {@link WidgetViewDelegate#onViewDestroy()}
     */
    @Deprecated
    public void onDestroy() {
        onViewDestroy();
    }
}
