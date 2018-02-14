package ru.surfstudio.standard.ui.base.configurator;

import ru.surfstudio.android.core.ui.base.dagger.CoreActivityModule;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.standard.app.App;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.app.dagger.AppComponent;
import ru.surfstudio.standard.app.dagger.DaggerActivityComponent;

/**
 * Created by makstuev on 30.01.2018. //todo
 */

public class ActivityConfigurator extends BaseActivityConfigurator<ActivityComponent, AppComponent> {

    @Override
    protected ActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .coreActivityModule(new CoreActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return ((App) getTargetActivity().getApplicationContext()).getAppComponent();

    }
}
