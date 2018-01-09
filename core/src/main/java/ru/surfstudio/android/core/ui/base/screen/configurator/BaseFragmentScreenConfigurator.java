package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.dagger.CoreFragmentScreenModule;

/**
 * Базовый класс конфигуратора экрана, основанного на Fragment, см {@link ScreenConfigurator}
 * @param <P> родительский даггер компонент
 * @param <M> даггер модуль для активити
 */
public abstract class BaseFragmentScreenConfigurator<P, M> extends ScreenConfigurator {

    private Bundle args;

    public BaseFragmentScreenConfigurator(Bundle args) {
        this.args = args;
    }

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M fragmentScreenModule,
                                                             CoreFragmentScreenModule coreFragmentScreenModule,
                                                             Bundle args);

    @Override
    public final ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getParentComponent(),
                getFragmentScreenModule(),
                new CoreFragmentScreenModule(getPersistentScreenScope()),
                args);
    }

    protected abstract M getFragmentScreenModule();

    protected abstract P getParentComponent();

    protected Bundle getArgs(){
        return args;
    }

}
