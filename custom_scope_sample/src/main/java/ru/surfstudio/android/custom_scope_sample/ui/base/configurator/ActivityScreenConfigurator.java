package ru.surfstudio.android.custom_scope_sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginScreenComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class ActivityScreenConfigurator
        extends BaseActivityViewConfigurator<LoginScreenComponent, ActivityComponent, ActivityScreenModule> {

    public ActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected ActivityComponent createActivityComponent(LoginScreenComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .loginScreenComponent(parentComponent)
                .activityModule(new ActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected LoginScreenComponent getParentComponent() {
        return LoginScopeStorage.INSTANCE.getLoginComponent();
    }

    @Override
    protected ActivityScreenModule getActivityScreenModule() {
        return new ActivityScreenModule(getPersistentScope());
    }
}
