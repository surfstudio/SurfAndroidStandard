package ru.surfstudio.android.filestorage.sample.ui.base.configurator;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.filestorage.sample.app.App;
import ru.surfstudio.android.filestorage.sample.app.dagger.AppComponent;
import ru.surfstudio.android.filestorage.sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.filestorage.sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.filestorage.sample.ui.base.dagger.activity.DaggerActivityComponent;

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
