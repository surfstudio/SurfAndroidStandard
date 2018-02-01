package ru.surfstudio.android.core.ui.base.screen.configurator;

import android.view.View;

import ru.surfstudio.android.core.ui.base.dagger.CoreWidgetScreenModule;
import ru.surfstudio.android.core.ui.base.screen.scope.WidgetPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;

/**
 * Базовый класс конфигуратора экрана, основанного на android.View, см {@link ViewConfigurator}
 * @param <P> родительский даггер компонент (ActivityComponent)
 * @param <M> даггер модуль для виджета
 */
public abstract class BaseWidgetViewConfigurator<P, M> implements ViewConfigurator<WidgetPersistentScope> {

    private CoreWidgetViewInterface target;
    private WidgetPersistentScope persistentScreenScope;

    public <T extends View & CoreWidgetViewInterface> BaseWidgetViewConfigurator(T target) {
        this.target = target;
    }

    protected abstract M getWidgetScreenModule();

    protected abstract P getParentComponent();

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M widgetScreenModule,
                                                             CoreWidgetScreenModule coreFragmentScreenModule);

    @Override
    public void setPersistentScope(WidgetPersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected WidgetPersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    @Override
    public void run() {
        satisfyDependencies(target);
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return getPersistentScope().getObject(ScreenComponent.class);
    }

    private void satisfyDependencies(CoreWidgetViewInterface target) {
        ScreenComponent component = getPersistentScope().getObject(ScreenComponent.class);
        if (component == null) {
            component = createScreenComponent();
            getPersistentScope().putObject(component, ScreenComponent.class);
        }
        component.inject(target);
    }

    private ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getParentComponent(),
                getWidgetScreenModule(),
                new CoreWidgetScreenModule(getPersistentScope()));
    }
}
