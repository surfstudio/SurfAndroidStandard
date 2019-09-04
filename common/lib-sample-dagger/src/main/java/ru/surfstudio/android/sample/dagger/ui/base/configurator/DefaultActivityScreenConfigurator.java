package ru.surfstudio.android.sample.dagger.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.sample.dagger.app.DefaultApp;
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DaggerDefaultActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class DefaultActivityScreenConfigurator
        extends BaseActivityViewConfigurator<DefaultAppComponent, DefaultActivityComponent, DefaultActivityScreenModule> {

    public DefaultActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected DefaultActivityComponent createActivityComponent(DefaultAppComponent parentComponent) {
        return DaggerDefaultActivityComponent.builder()
                .defaultAppComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected DefaultAppComponent getParentComponent() {
        return ((DefaultApp) (getTargetActivity()).getApplicationContext()).getDefaultAppComponent();
    }

    @Override
    protected DefaultActivityScreenModule getActivityScreenModule() {
        return new DefaultActivityScreenModule(getPersistentScope());
    }
}
