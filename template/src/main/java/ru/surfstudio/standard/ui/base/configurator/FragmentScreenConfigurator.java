package ru.surfstudio.standard.ui.base.configurator;

import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.FragmentScreenModule;

/**
 * Created by makstuev on 30.01.2018. //todo
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
