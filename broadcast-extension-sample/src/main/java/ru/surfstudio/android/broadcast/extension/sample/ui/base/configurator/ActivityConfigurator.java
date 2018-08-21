package ru.surfstudio.android.broadcast.extension.sample.ui.base.configurator;

import ru.surfstudio.android.broadcast.extension.sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.broadcast.extension.sample.app.App;
import ru.surfstudio.android.broadcast.extension.sample.app.dagger.AppComponent;
import ru.surfstudio.android.broadcast.extension.sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.broadcast.extension.sample.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;

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
