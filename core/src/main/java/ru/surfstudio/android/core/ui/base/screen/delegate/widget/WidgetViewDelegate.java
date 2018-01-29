package ru.surfstudio.android.core.ui.base.screen.delegate.widget;


import ru.surfstudio.android.core.ui.base.screen.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.scope.WidgetPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;

public class WidgetViewDelegate {

    private CoreWidgetViewInterface coreWidgetView;
    private BaseWidgetViewConfigurator configurator;
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
        initConfigurator();
        initPersistentScope();
        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();
    }

    public void onDestroy() {
        if (getScreenState().isCompletelyDestroyed()) { //todo comment
            scopeStorage.removeScope(getName());
        }
    }

    protected WidgetPersistentScope getPersistentScope() {
        return scopeStorage.getWidgetScope(getName());
    }

    private void initConfigurator() {
        configurator = coreWidgetView.createConfigurator();
    }

    private void runConfigurator() {
        configurator.run();
    }

    private void initPersistentScope() {
        if (getPersistentScope() == null) {
            PersistentScope parentScope = parentPersistentScopeFinder.find();
            WidgetPersistentScope persistentScope = new WidgetPersistentScope(
                    getName(),
                    parentScope.getScreenEventDelegateManager(),
                    parentScope.getScreenState());
            scopeStorage.putScope(persistentScope);
        }
        configurator.setPersistentScope(getPersistentScope());
    }

    //getters

    public BaseWidgetViewConfigurator getConfigurator() {
        return configurator;
    }

    private ScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    private String getName() {
        return configurator.getName();
    }

}
