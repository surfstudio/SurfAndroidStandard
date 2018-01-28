package ru.surfstudio.android.core.ui.base.screen.widjet;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseWigetViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * Created by makstuev on 28.01.2018.
 */

public interface WidjetViewInterface extends PresenterHolderCoreView, HasConfigurator {
    void init();

    void onCreate();

    @Override
    BaseWigetViewConfigurator createConfigurator();

    @Override
    BaseWigetViewConfigurator getConfigurator();

    FragmentDelegate createFragmentDelegate();
}
