package ru.surfstudio.android.core.ui.base.screen.configurator;

import android.app.Activity;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;

/**
 * Базовый конфигуратор для активити
 * Создает ActivityComponent
 * Предоставляет уникальное имя экрана, для корневой логики экрана
 * @param <A> тип ActivityComponent
 * @param <P> тип родительского компонента, обычно AppComponent
 */
public abstract class BaseActivityConfigurator<A, P> implements Configurator {
    private ActivityPersistentScope persistentScreenScope;
    private A activityComponent;

    protected abstract A createActivityComponent(P parentComponent);
    protected abstract P getParentComponent();

    @Override
    public void run(){
        init();
    }

    protected ActivityPersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    protected void init() {
        if (activityComponent == null) {
            activityComponent = createActivityComponent(getParentComponent());
        }
    }

    public A getActivityComponent() {
        return activityComponent;
    }

    public void setPersistentScope(ActivityPersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected <T extends Activity & CoreActivityInterface> T getTargetActivity() {
        return (T)getPersistentScope().getScreenState().getCoreActivity();
    }

}
