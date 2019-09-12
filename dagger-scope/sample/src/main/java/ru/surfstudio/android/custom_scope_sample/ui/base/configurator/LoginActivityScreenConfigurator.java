package ru.surfstudio.android.custom_scope_sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerLoginActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.LoginActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.LoginActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class LoginActivityScreenConfigurator
        extends BaseActivityViewConfigurator<LoginComponent, LoginActivityComponent, ActivityScreenModule> {

    public LoginActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected LoginActivityComponent createActivityComponent(LoginComponent parentComponent) {
        return DaggerLoginActivityComponent.builder()
                .loginComponent(parentComponent)
                .activityModule(new ActivityModule(getPersistentScope()))
                .loginActivityModule(new LoginActivityModule())
                .build();
    }

    @Override
    protected LoginComponent getParentComponent() {
        return LoginScopeStorage.INSTANCE.getLoginComponent();
    }

    @Override
    protected ActivityScreenModule getActivityScreenModule() {
        return new ActivityScreenModule(getPersistentScope());
    }
}
