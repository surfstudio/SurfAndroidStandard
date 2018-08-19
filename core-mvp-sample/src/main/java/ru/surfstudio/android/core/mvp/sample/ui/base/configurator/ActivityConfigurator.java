package ru.surfstudio.android.core.mvp.sample.ui.base.configurator;

import ru.surfstudio.android.core.mvp.sample.app.App;
import ru.surfstudio.android.core.mvp.sample.app.dagger.AppComponent;
import ru.surfstudio.android.core.mvp.sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.core.mvp.sample.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.mvp.sample.ui.base.dagger.activity.ActivityComponent;

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
        return ((App) getTargetActivity().getApplicationContext()).getAppComponent();
    }
}
