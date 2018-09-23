package ru.surfstudio.android.custom_scope_sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.custom_scope_sample.app.AppConfigurator;
import ru.surfstudio.android.custom_scope_sample.app.dagger.AppComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class ActivityScreenConfigurator
        extends BaseActivityViewConfigurator<AppComponent, ActivityComponent, ActivityScreenModule> {

    public ActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected ActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .activityModule(new ActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return AppConfigurator.INSTANCE.getAppComponent();
    }

    @Override
    protected ActivityScreenModule getActivityScreenModule() {
        return new ActivityScreenModule(getPersistentScope());
    }
}
