package ru.surfstudio.standard.ui.base.configurator;

import android.os.Bundle;

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.standard.ui.base.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.FragmentScreenModule;

/**
 * Базовый конфигуратор для экрана, основанного на фрагменте
 */

public abstract class FragmentScreenConfigurator
        extends BaseFragmentViewConfigurator<ActivityComponent, FragmentScreenModule> {

    public FragmentScreenConfigurator(Bundle args) {
        super(args);
    }

    @Override
    protected FragmentScreenModule getFragmentScreenModule() {
        return new FragmentScreenModule();
    }

    @Override
    protected ActivityComponent getParentComponent() {
        return (ActivityComponent) ((CoreActivityInterface) getTargetFragmentView().getActivity())
                .getPersistentScope()
                .getConfigurator()
                .getActivityComponent();
    }
}
