package ru.surfstudio.android.custom_scope_sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.custom_scope_sample.app.AppInjector;
import ru.surfstudio.android.custom_scope_sample.app.dagger.AppComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerSpecialActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.SpecialActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.SpecialActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class SpecialActivityScreenConfigurator
        extends BaseActivityViewConfigurator<AppComponent, SpecialActivityComponent, ActivityScreenModule> {

    public SpecialActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected SpecialActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerSpecialActivityComponent.builder()
                .appComponent(parentComponent)
                .activityModule(new ActivityModule(getPersistentScope()))
                .specialActivityModule(new SpecialActivityModule())
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return AppInjector.INSTANCE.getAppComponent();
    }

    @Override
    protected ActivityScreenModule getActivityScreenModule() {
        return new ActivityScreenModule(getPersistentScope());
    }
}
