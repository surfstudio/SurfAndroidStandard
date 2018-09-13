package ru.surfstudio.android.sample.dagger.ui.base.configurator;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.sample.dagger.app.DefaultApp;
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DaggerDefaultActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;

/**
 * Базовый конфигуратор для активити
 */

public class DefaultActivityConfigurator extends BaseActivityConfigurator<DefaultActivityComponent, DefaultAppComponent> {

    @Override
    protected DefaultActivityComponent createActivityComponent(DefaultAppComponent parentComponent) {
        return DaggerDefaultActivityComponent.builder()
                .defaultAppComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected DefaultAppComponent getParentComponent() {
        return ((DefaultApp) getTargetActivity().getApplicationContext()).getDefaultAppComponent();
    }
}
