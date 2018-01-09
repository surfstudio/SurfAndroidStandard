package ru.surfstudio.android.core.ui.base.screen.configurator;

import com.agna.ferro.core.PersistentScreenScope;

public abstract class BaseActivityConfigurator<C, P> {
    private final String ACTIVITY_COMPONENT_TAG = "ACTIVITY_COMPONENT_TAG";

    private PersistentScreenScope persistentScreenScope;

    public void setPersistentScreenScope(PersistentScreenScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected abstract C createActivityComponent(P parentComponent);
    protected abstract P getParentComponent();

    public void init() {
        C activityComponent = persistentScreenScope.getObject(ACTIVITY_COMPONENT_TAG);
        if (activityComponent == null) {
            activityComponent = createActivityComponent(getParentComponent());
            persistentScreenScope.putObject(activityComponent, ACTIVITY_COMPONENT_TAG);
        }
    }

    public C getActivityComponent() {
        return persistentScreenScope.getObject(ACTIVITY_COMPONENT_TAG);
    }

}
