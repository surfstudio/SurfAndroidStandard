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
        if (!scopeStorage.isExist(getScopeId())) {
            ScreenPersistentScope parentScope = parentPersistentScopeFinder.find();
            if (parentScope == null) {
                throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
            }
            WidgetScreenState screenState = new WidgetScreenState(parentScope.getScreenState());
            BaseWidgetViewConfigurator configurator = coreWidgetView.createConfigurator();

            parentScopeId = parentScope.getScopeId();
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
