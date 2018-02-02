package ru.surfstudio.android.core.ui.base.screen.widjet;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.widget.WidgetViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * Created by makstuev on 28.01.2018.
 */

public interface CoreWidgetViewInterface extends PresenterHolderCoreView, HasConfigurator {

    @Override
    BaseWidgetViewConfigurator createConfigurator();

    @Override
    BaseWidgetViewConfigurator getConfigurator();

    WidgetViewDelegate createWidgetViewDelegate();

    void init();

    void onCreate();
}
