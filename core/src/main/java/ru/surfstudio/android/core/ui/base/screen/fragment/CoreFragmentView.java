package ru.surfstudio.android.core.ui.base.screen.fragment;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;

/**
 * Base class with core logic for view, based on Fragment
 */
public abstract class CoreFragmentView extends CoreFragment
        implements CoreFragmentViewInterface {

    protected abstract CorePresenter[] getPresenters();

    @Override
    public abstract BaseFragmentViewConfigurator createConfigurator();

    @Override
    public FragmentViewDelegate createFragmentDelegate() {
        return ScreenDelegateFactoryContainer.get().createFragmentViewDelegate(this);
    }

    @Override
    public BaseFragmentViewConfigurator getConfigurator() {
        return (BaseFragmentViewConfigurator) super.getConfigurator();
    }

    /**
     * Override this instead {@link #onActivityCreated(Bundle)}
     *
     * @param viewRecreated show whether view created in first time or recreated after
     *                      changing configuration
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState, boolean viewRecreated) {

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
