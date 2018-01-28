package ru.surfstudio.android.core.ui.base.screen.configurator;

import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;

public abstract class BaseActivityConfigurator<A, P> implements Configurator {
    private final String ACTIVITY_COMPONENT_TAG = "ACTIVITY_COMPONENT_TAG";

    private PersistentScope persistentScreenScope;
    private FragmentActivity target;

    public BaseActivityConfigurator(FragmentActivity target) {
        this.target = target;
    }

    protected abstract A createActivityComponent(P parentComponent);
    protected abstract P getParentComponent();

    @Override
    public void run(){
        init();
    }

    @Override
    public String getName() {
        return target.getClass().getCanonicalName();
    }

    protected PersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    protected void init() {
        A activityComponent = (A) persistentScreenScope.getObject(ACTIVITY_COMPONENT_TAG);
        if (activityComponent == null) {
            activityComponent = createActivityComponent(getParentComponent());
            persistentScreenScope.putObject(activityComponent, ACTIVITY_COMPONENT_TAG);
        }
    }

    public A getActivityComponent() {
        return (A) persistentScreenScope.getObject(ACTIVITY_COMPONENT_TAG);
    }

    @Override
    public void setPersistentScope(PersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

}
