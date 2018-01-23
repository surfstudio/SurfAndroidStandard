package ru.surfstudio.android.core.ui.base.screen.activity;


import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.BaseActivityDelegate;

public interface BaseActivityInterface extends HasName {

    BaseActivityConfigurator createActivityConfigurator();

    BaseActivityDelegate getBaseActivityDelegate();

    BaseActivityDelegate createBaseActivityDelegate();
}
