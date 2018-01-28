package ru.surfstudio.android.core.ui.base.screen.activity;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * инрефейс для вью, которая оповещает презентер о событиях жизненного цикла экрана
 */
public interface CoreActivityViewInterface extends
        PresenterHolderCoreView,
        CoreActivityInterface {

    @Override
    BaseActivityViewConfigurator createConfigurator();

    @Override
    BaseActivityViewConfigurator getConfigurator();

    @Override
    ActivityViewDelegate createActivityDelegate();

}
