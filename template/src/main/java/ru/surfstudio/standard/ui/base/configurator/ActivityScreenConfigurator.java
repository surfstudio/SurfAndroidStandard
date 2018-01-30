package ru.surfstudio.standard.ui.base.configurator;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.dagger.CoreActivityModule;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.standard.app.App;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.app.dagger.AppComponent;
import ru.surfstudio.standard.app.dagger.DaggerActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule;

/**
 * Created by makstuev on 30.01.2018. //todo
 */

public abstract class ActivityScreenConfigurator
        extends BaseActivityViewConfigurator<AppComponent, ActivityComponent, ActivityScreenModule> {

    private final FragmentActivity target;

    public <T extends FragmentActivity & CoreActivityViewInterface> ActivityScreenConfigurator(T target, Intent intent) {
        super(target, intent);
        this.target = target;
    }

    @Override
    protected ActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .coreActivityModule(new CoreActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return ((App) target.getApplicationContext()).getAppComponent();
    }

    @Override
    protected ActivityScreenModule getActivityScreenModule() {
        return new ActivityScreenModule();
    }
}
