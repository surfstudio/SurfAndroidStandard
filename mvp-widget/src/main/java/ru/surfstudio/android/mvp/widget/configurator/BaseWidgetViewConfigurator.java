package ru.surfstudio.android.mvp.widget.configurator;

import android.view.View;

import ru.surfstudio.android.core.mvp.configurator.ScreenComponent;
import ru.surfstudio.android.core.mvp.configurator.ViewConfigurator;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * Базовый класс конфигуратора экрана, основанного на android.View, см {@link ViewConfigurator}
 *
 * @param <P> родительский даггер компонент (ActivityComponent)
 * @param <M> даггер модуль для виджета
 */
public abstract class BaseWidgetViewConfigurator<P, M> implements ViewConfigurator {

    private WidgetViewPersistentScope persistentScreenScope;
    private ScreenComponent component;

    public BaseWidgetViewConfigurator() {
    }

    protected abstract M getWidgetScreenModule();

    protected abstract P getParentComponent();

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M widgetScreenModule);

    protected <T extends View & CoreWidgetViewInterface> T getTargetWidgetView() {
        return (T) getPersistentScope().getScreenState().getCoreWidget();
    }

    protected WidgetViewPersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    public void setPersistentScope(WidgetViewPersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    @Override
    public void run() {
        satisfyDependencies(getPersistentScope().getScreenState().getCoreWidget());
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return component;
    }

    private void satisfyDependencies(CoreWidgetViewInterface target) {
        if (component == null) {
            component = createScreenComponent();
        }
        component.inject(target);
    }

    private ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getParentComponent(),
                getWidgetScreenModule());
    }
}
