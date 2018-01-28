package ru.surfstudio.android.core.ui.base.screen.activity;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityDelegate;

public interface CoreActivityInterface extends
        HasName,
        HasConfigurator {

    @Override
    BaseActivityConfigurator createConfigurator();

    @Override
    BaseActivityConfigurator getConfigurator();

    ActivityDelegate createActivityDelegate();

    /**
     * @param viewRecreated render whether view created in first time or recreated after
     *                      changing configuration
     */
    void onCreate(Bundle savedInstanceState, boolean viewRecreated);
}
