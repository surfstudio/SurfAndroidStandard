package ru.surfstudio.standard.ui.base.configurator;

import android.content.Context;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.standard.app.App;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.app.dagger.AppComponent;
import ru.surfstudio.standard.app.dagger.DaggerActivityComponent;


public class ActivityConfigurator extends BaseActivityConfigurator<ActivityComponent, AppComponent> {

    private Context context;

    public ActivityConfigurator(Context context) {
        this.context = context;
    }

    @Override
    protected ActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return ((App)context.getApplicationContext()).getAppComponent();
    }
}
