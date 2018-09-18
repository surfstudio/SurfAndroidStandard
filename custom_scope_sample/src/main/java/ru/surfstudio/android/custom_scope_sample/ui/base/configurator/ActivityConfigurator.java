package ru.surfstudio.android.custom_scope_sample.ui.base.configurator;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityModule;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.DaggerActivityComponent;
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginScreenComponent;

/**
 * Базовый конфигуратор для активити
 */

public class ActivityConfigurator extends BaseActivityConfigurator<ActivityComponent, LoginScreenComponent> {

    @Override
    protected ActivityComponent createActivityComponent(LoginScreenComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .loginScreenComponent(parentComponent)
                .activityModule(new ActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected LoginScreenComponent getParentComponent() {
        return LoginScopeStorage.INSTANCE.getLoginScreenComponent();
    }
}
