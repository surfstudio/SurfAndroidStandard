package ru.surfstudio.android.sample.dagger.ui.base.configurator;

import android.os.Bundle;

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на фрагменте
 */

public abstract class DefaultFragmentScreenConfigurator
        extends BaseFragmentViewConfigurator<DefaultActivityComponent, DefaultFragmentScreenModule> {

    public DefaultFragmentScreenConfigurator(Bundle args) {
        super(args);
    }

    @Override
    protected DefaultFragmentScreenModule getFragmentScreenModule() {
        return new DefaultFragmentScreenModule(getPersistentScope());
    }

    @Override
    protected DefaultActivityComponent getParentComponent() {
        return (DefaultActivityComponent) ((CoreActivityInterface) getTargetFragmentView().getActivity())
                .getPersistentScope()
                .getConfigurator()
                .getActivityComponent();
    }
}
