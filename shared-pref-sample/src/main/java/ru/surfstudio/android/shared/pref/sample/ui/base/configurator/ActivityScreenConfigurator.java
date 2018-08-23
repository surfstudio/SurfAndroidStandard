package ru.surfstudio.android.shared.pref.sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule;
import ru.surfstudio.android.shared.pref.sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.shared.pref.sample.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.android.shared.pref.sample.app.App;
import ru.surfstudio.android.shared.pref.sample.app.dagger.AppComponent;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class ActivityScreenConfigurator
        extends BaseActivityViewConfigurator<AppComponent, ActivityComponent, DefaultActivityScreenModule> {

    public ActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected ActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return ((App) (getTargetActivity()).getApplicationContext()).getAppComponent();
    }

    @Override
    protected DefaultActivityScreenModule getActivityScreenModule() {
        return new DefaultActivityScreenModule(getPersistentScope());
    }
}
