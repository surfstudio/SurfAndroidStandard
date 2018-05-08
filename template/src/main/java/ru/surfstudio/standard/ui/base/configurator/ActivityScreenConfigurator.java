package ru.surfstudio.standard.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.standard.app.App;
import ru.surfstudio.standard.app.dagger.AppComponent;
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.standard.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.screen.ActivityScreenModule;

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
        return ((App)(getTargetActivity()).getApplicationContext()).getAppComponent();
    }

    @Override
    protected ActivityScreenModule getActivityScreenModule() {
        return new ActivityScreenModule(getPersistentScope());
    }
}
