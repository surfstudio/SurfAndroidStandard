package ru.surfstudio.android.shared.pref.sample.ui.base.configurator;

import android.os.Bundle;

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule;
import ru.surfstudio.android.shared.pref.sample.ui.base.dagger.activity.ActivityComponent;

/**
 * Базовый конфигуратор для экрана, основанного на фрагменте
 */

public abstract class FragmentScreenConfigurator
        extends BaseFragmentViewConfigurator<ActivityComponent, DefaultFragmentScreenModule> {

    public FragmentScreenConfigurator(Bundle args) {
        super(args);
    }

    @Override
    protected DefaultFragmentScreenModule getFragmentScreenModule() {
        return new DefaultFragmentScreenModule(getPersistentScope());
    }

    @Override
    protected ActivityComponent getParentComponent() {
        return (ActivityComponent) ((CoreActivityInterface) getTargetFragmentView().getActivity())
                .getPersistentScope()
                .getConfigurator()
                .getActivityComponent();
    }
}
