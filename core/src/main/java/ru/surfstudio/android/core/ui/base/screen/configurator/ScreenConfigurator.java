package ru.surfstudio.android.core.ui.base.screen.configurator;


import ru.surfstudio.android.core.HasName;
import ru.surfstudio.android.core.ui.base.scope.PersistentScope;

import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * Базовый класс конфигуратора экрана, инкапсулирует всю логику работы с даггером
 * @param <V> конкретный тип вью
 */
public abstract class ScreenConfigurator<V extends PresenterHolderCoreView> implements HasName {

    private PersistentScope persistentScreenScope;

    public abstract ScreenComponent createScreenComponent();

    public void setPersistentScope(PersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected PersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    public ScreenComponent getScreenComponent() {
        return persistentScreenScope.getObject(ScreenComponent.class);
    }

    public void satisfyDependencies(V target) {
        ScreenComponent component = persistentScreenScope.getObject(ScreenComponent.class);
        if (component == null) {
            component = createScreenComponent();
            persistentScreenScope.putObject(component, ScreenComponent.class);
        }
        component.inject(target);
    }
}
