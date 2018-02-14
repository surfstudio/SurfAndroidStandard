package ru.surfstudio.android.core.ui.base.screen.delegate.widget;


import ru.surfstudio.android.core.ui.base.screen.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.WidgetScreenState;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;

/**
 * делегат для widget вью,
 * управляет ключевыми сущностями внутренней логики виджета:
 * - PersistentScope
 * - ScreenEventDelegateManager - соответствует менеджеру экрана-контейнера
 * - ScreenState - соответствует ScreenState экрана-контейнера
 * - ScreenConfigurator
 */
public class WidgetViewDelegate {

    private CoreWidgetViewInterface coreWidgetView;
    private PersistentScopeStorage scopeStorage;
    private ParentPersistentScopeFinder parentPersistentScopeFinder;

    public WidgetViewDelegate(CoreWidgetViewInterface coreWidgetView,
                              PersistentScopeStorage scopeStorage,
                              ParentPersistentScopeFinder parentPersistentScopeFinder) {
        this.coreWidgetView = coreWidgetView;
        this.scopeStorage = scopeStorage;
        this.parentPersistentScopeFinder = parentPersistentScopeFinder;
    }

    public void onCreate() {
        initPersistentScope();
        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();
    }

    public void onDestroy() {
        if (getScreenState().isCompletelyDestroyed()) {
            scopeStorage.remove(getName());
        }
    }

    protected WidgetViewPersistentScope getPersistentScope() {
        return scopeStorage.get(getName(), WidgetViewPersistentScope.class);
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        if (getPersistentScope() == null) {
            PersistentScope parentScope = parentPersistentScopeFinder.find();
            if (parentScope == null) {
                throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
            }
            WidgetScreenState screenState = new WidgetScreenState(parentScope.getScreenState());
            BaseWidgetViewConfigurator configurator = coreWidgetView.createConfigurator();
            WidgetViewPersistentScope persistentScope = new WidgetViewPersistentScope(
                    parentScope.getScreenEventDelegateManager(),
                    screenState,
                    configurator,
                    coreWidgetView.getName());
            configurator.setPersistentScope(persistentScope);
            scopeStorage.put(persistentScope);
        }
    }

    //getters

    private WidgetScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    private String getName() {
        return coreWidgetView.getName();
    }
}
