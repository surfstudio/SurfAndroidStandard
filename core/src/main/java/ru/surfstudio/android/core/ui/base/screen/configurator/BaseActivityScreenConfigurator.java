package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule;

/**
 * Базовый класс конфигуратора экрана, основанного на Activity, см {@link ScreenConfigurator}
 *
 * @param <P> родительский даггер компонент
 * @param <M> даггер модуль для активити
 */
public abstract class BaseActivityScreenConfigurator<P, M> extends ScreenConfigurator {

    private Intent intent;

    public BaseActivityScreenConfigurator(Intent intent) {
        this.intent = intent;
    }

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M activityScreenModule,
                                                             CoreActivityScreenModule coreActivityScreenModule,
                                                             Intent intent);

    @Override
    public final ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getParentComponent(),
                getActivityScreenModule(),
                new CoreActivityScreenModule(getPersistentScreenScope()),
                intent);
    }

    protected abstract P getParentComponent();

    protected abstract M getActivityScreenModule();

    protected Intent getIntent() {
        return intent;
    }
}
