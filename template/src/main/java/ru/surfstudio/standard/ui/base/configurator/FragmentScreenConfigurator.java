package ru.surfstudio.standard.ui.base.configurator;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.FragmentScreenModule;

/**
 * Created by makstuev on 30.01.2018. //todo
 */

public abstract class FragmentScreenConfigurator
        extends BaseFragmentViewConfigurator<ActivityComponent, FragmentScreenModule> {

    private final Fragment target;

    public <T extends Fragment & CoreFragmentViewInterface> FragmentScreenConfigurator(T target, Bundle args) {
        super(target, args);
        this.target = target;
    }

    @Override
    protected FragmentScreenModule getFragmentScreenModule() {
        return new FragmentScreenModule();
    }

    @Override
    protected ActivityComponent getParentComponent() {
        return (ActivityComponent) ((CoreActivityInterface) target.getActivity())
                .getConfigurator()
                .getActivityComponent();
    }
}
