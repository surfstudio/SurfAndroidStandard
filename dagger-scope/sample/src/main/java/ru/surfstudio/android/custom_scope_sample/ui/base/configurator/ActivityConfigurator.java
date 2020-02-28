package ru.surfstudio.android.custom_scope_sample.ui.base.configurator;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.custom_scope_sample.app.AppConfigurator;
import ru.surfstudio.android.custom_scope_sample.app.dagger.AppComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerActivityComponent;

/**
 * Базовый конфигуратор для активити
 */

public class ActivityConfigurator extends BaseActivityConfigurator<ActivityComponent, AppComponent> {

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
}
