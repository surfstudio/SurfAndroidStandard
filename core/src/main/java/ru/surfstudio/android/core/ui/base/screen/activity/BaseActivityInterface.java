package ru.surfstudio.android.core.ui.base.screen.activity;

import com.agna.ferro.core.HasName;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.BaseActivityDelegate;

public interface BaseActivityInterface extends HasName {

    BaseActivityConfigurator createActivityConfigurator();

    BaseActivityDelegate getBaseActivityDelegate();
}
