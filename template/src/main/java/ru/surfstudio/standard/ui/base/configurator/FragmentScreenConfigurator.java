package ru.surfstudio.standard.ui.base.configurator;

import android.app.Activity;
import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentScreenConfigurator;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.FragmentScreenModule;

public abstract class FragmentScreenConfigurator extends BaseFragmentScreenConfigurator<ActivityComponent, FragmentScreenModule> {

    private final BaseActivityInterface baseActivity;

    public FragmentScreenConfigurator(Activity baseActivity, Bundle args) {
        super(args);
        this.baseActivity = (BaseActivityInterface) baseActivity;
    }

    @Override
    protected ActivityComponent getParentComponent() {
        return ((ActivityConfigurator)baseActivity.getBaseActivityDelegate().getScreenConfigurator()).getActivityComponent();
    }

    @Override
    protected FragmentScreenModule getFragmentScreenModule() {
        return new FragmentScreenModule();
    }

}
