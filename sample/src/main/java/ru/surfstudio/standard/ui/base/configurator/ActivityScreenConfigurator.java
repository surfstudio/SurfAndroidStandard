package ru.surfstudio.standard.ui.base.configurator;

import android.app.Activity;
import android.content.Intent;

import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityScreenConfigurator;
import ru.surfstudio.standard.app.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule;

public abstract class ActivityScreenConfigurator extends BaseActivityScreenConfigurator<ActivityComponent, ActivityScreenModule> {

    private final BaseActivityInterface baseActivity;

    public ActivityScreenConfigurator(Activity baseActivity, Intent intent) {
        super(intent);
        this.baseActivity = (BaseActivityInterface) baseActivity;
    }

    @Override
    protected ActivityComponent getParentComponent() {
        return ((ActivityConfigurator)baseActivity.getBaseActivityDelegate().getScreenConfigurator()).getActivityComponent();
    }

    @Override
    protected ActivityScreenModule getActivityScreenModule() {
        return new ActivityScreenModule();
    }
}
