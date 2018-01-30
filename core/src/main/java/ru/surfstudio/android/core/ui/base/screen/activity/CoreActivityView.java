package ru.surfstudio.android.core.ui.base.screen.activity;

import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;

/**
 * Base class with core logic for view, based on Activity
 */
public abstract class CoreActivityView extends CoreActivity implements
        CoreActivityViewInterface {

    protected abstract CorePresenter[] getPresenters();

    @Override
    public ActivityViewDelegate createActivityDelegate() {
        return ScreenDelegateFactoryContainer.get().createActivityViewDelegate(this);
    }

    @Override
    public BaseActivityViewConfigurator getConfigurator() {
        return (BaseActivityViewConfigurator) super.getConfigurator();
    }

    /**
     * Override this instead {@link #onCreate(Bundle)}
     *
     * @param viewRecreated render whether view created in first time or recreated after
     *                      changing configuration
     */
    @Override
    public void onCreate(Bundle savedInstanceState, boolean viewRecreated) {

    }

    /**
     * Bind presenter to this view
     * You can override this method for support different presenters for different views
     */
    @Override
    public void bindPresenters() {
        for (CorePresenter presenter : getPresenters()) {
            presenter.attachView(this);
        }
    }
}
