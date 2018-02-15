package ru.surfstudio.android.core.ui.base.screen.configurator;

import ru.surfstudio.android.core.ui.base.dagger.CoreWidgetScreenModule;
import ru.surfstudio.android.core.ui.base.screen.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;

/**
 * Базовый класс конфигуратора экрана, основанного на android.View, см {@link ViewConfigurator}
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
                                                             M widgetScreenModule,
                                                             CoreWidgetScreenModule coreFragmentScreenModule);

    public void setPersistentScope(WidgetViewPersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected WidgetViewPersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    @Override
    public void run() {
        satisfyDependencies(getPersistentScope().getScreenState().getCoreWidget());
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return getPersistentScope().getObject(ScreenComponent.class);
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
                getWidgetScreenModule(),
                new CoreWidgetScreenModule(getPersistentScope()));
    }
}
