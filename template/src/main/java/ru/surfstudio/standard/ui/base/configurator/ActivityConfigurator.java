package ru.surfstudio.standard.ui.base.configurator;

import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.standard.app.App;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.app.dagger.AppComponent;

/**
 * Created by makstuev on 30.01.2018. //todo
 */

public class ActivityConfigurator extends BaseActivityConfigurator<ActivityComponent, AppComponent> {


    private FragmentActivity target;

    public <T extends FragmentActivity & CoreActivityViewInterface> ActivityConfigurator(T target) {
        super(target);
        this.target = target;
    }

    @Override
    protected ActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return ((App) target.getApplicationContext()).getAppComponent();

    }
}
