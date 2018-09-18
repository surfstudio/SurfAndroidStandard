package ru.surfstudio.android.custom_scope_sample.ui.base.configurator;

import android.content.Intent;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerSpecialActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.SpecialActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.SpecialActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginScreenComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на активити
 */

public abstract class SpecialActivityScreenConfigurator
        extends BaseActivityViewConfigurator<LoginScreenComponent, SpecialActivityComponent, ActivityScreenModule> {

    public SpecialActivityScreenConfigurator(Intent intent) {
        super(intent);
    }

    @Override
    protected SpecialActivityComponent createActivityComponent(LoginScreenComponent parentComponent) {
        return DaggerSpecialActivityComponent.builder()
                .loginScreenComponent(parentComponent)
                .activityModule(new ActivityModule(getPersistentScope()))
                .specialActivityModule(new SpecialActivityModule())
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
