package ru.surfstudio.android.push.sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.push.sample.app.AppConfigurator;
import ru.surfstudio.android.push.sample.app.dagger.CustomAppComponent;
import ru.surfstudio.android.push.sample.ui.base.dagger.activity.CustomActivityComponent;
import ru.surfstudio.android.push.sample.ui.base.dagger.activity.DaggerCustomActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class CustomActivityScreenConfigurator
        extends BaseActivityViewConfigurator<CustomAppComponent, CustomActivityComponent, DefaultActivityScreenModule> {

    public CustomActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected CustomActivityComponent createActivityComponent(CustomAppComponent parentComponent) {
        return DaggerCustomActivityComponent.builder()
                .customAppComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected CustomAppComponent getParentComponent() {
        return AppConfigurator.INSTANCE.getCustomAppComponent();
    }

    @Override
    protected DefaultActivityScreenModule getActivityScreenModule() {
        return new DefaultActivityScreenModule(getPersistentScope());
    }
}