package ru.surfstudio.android.easyadapter.sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.easyadapter.sample.app.CustomApp;
import ru.surfstudio.android.easyadapter.sample.app.dagger.CustomAppComponent;
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity.CustomActivityComponent;
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity.DaggerCustomActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule;

/**
 * Base configurator for screen which is based on Activity
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
        return ((CustomApp) (getTargetActivity()).getApplicationContext()).getCustomAppComponent();
    }

    @Override
    protected DefaultActivityScreenModule getActivityScreenModule() {
        return new DefaultActivityScreenModule(getPersistentScope());
    }
}
