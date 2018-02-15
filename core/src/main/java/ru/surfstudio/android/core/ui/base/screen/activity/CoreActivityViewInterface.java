package ru.surfstudio.android.core.ui.base.screen.activity;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * инрефейс для вью, основанной на активити
 */
public interface CoreActivityViewInterface extends
        PresenterHolderCoreView,
        CoreActivityInterface {

    @Override
    BaseActivityViewConfigurator createConfigurator();

    @Override
    ActivityViewPersistentScope getPersistentScope();

    @Override
    ActivityViewDelegate createActivityDelegate();

}
