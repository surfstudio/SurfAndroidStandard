package ru.surfstudio.android.security.sample.ui.base.configurator;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;
import ru.surfstudio.android.security.sample.app.CustomApp;
import ru.surfstudio.android.security.sample.app.dagger.CustomAppComponent;
import ru.surfstudio.android.security.sample.ui.base.dagger.activity.CustomActivityComponent;
import ru.surfstudio.android.security.sample.ui.base.dagger.activity.DaggerCustomActivityComponent;

/**
 * Базовый конфигуратор для активити
 */
public class CustomActivityConfigurator extends BaseActivityConfigurator<CustomActivityComponent, CustomAppComponent> {

    @Override
    protected CustomActivityComponent createActivityComponent(CustomAppComponent parentComponent) {
        return DaggerCustomActivityComponent.builder()
                .customAppComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected CustomAppComponent getParentComponent() {
        return ((CustomApp) getTargetActivity().getApplicationContext()).getCustomAppComponent();
    }
}
