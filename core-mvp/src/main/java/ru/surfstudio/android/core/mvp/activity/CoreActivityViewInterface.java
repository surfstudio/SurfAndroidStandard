package ru.surfstudio.android.core.mvp.activity;

import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.mvp.delegate.ActivityViewDelegate;
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.mvp.view.PresenterHolderCoreView;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;

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
