package ru.surfstudio.android.core.ui.base.screen.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.agna.ferro.core.PersistentScreenScope;

import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivity;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.MvpFragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.MvpViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.android.core.ui.base.screen.view.ContentContainerView;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderFragmentCoreView;

/**
 * Base class with core logic for view, based on Fragment
 */
public abstract class CoreFragmentView extends BaseFragment implements
        PresenterHolderFragmentCoreView {

    private MvpViewDelegate viewDelegate;

    /**
     * Override this instead {@link #onActivityCreated(Bundle)}
     *
     * @param viewRecreated show whether view created in first time or recreated after
     *                      changing configuration
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState, boolean viewRecreated) {

    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewDelegate = new MvpFragmentViewDelegate(getActivity(), this, this);
        viewDelegate.onPreMvpViewCreate();
        viewDelegate.onMvpViewCreate(savedInstanceState, null);
    }

    @Override
    public ScreenConfigurator getScreenConfigurator() {
        return viewDelegate.getScreenConfigurator();
    }

    /**
     * A wrapper method for internal use.
     * Routed to {@link BaseFragmentScreenConfigurator}
     *
     * @return the arguments the fragment started with
     * @see Fragment#getArguments()
     */
    @Override
    public final Bundle getStartArgs() {
        return getArguments();
    }

    @Override
    public String getName() {
        getStartArgs();

        return getScreenConfigurator().getName();
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

    @Override
    public void onStart() {
        super.onStart();
        viewDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewDelegate.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroy();
    }

    public boolean onBackPressed() {
        if (this instanceof ContentContainerView) {
            Fragment fragment = getChildFragmentManager().findFragmentById(((ContentContainerView) this).getContentContainerViewId());
            if (fragment instanceof CoreFragmentView && ((CoreFragmentView) fragment).onBackPressed()) {
                return true;
            }

            if (fragment != null && getChildFragmentManager().popBackStackImmediate()) {
                if (fragment instanceof CoreFragmentView) {
                    // успешное удалив фрагмент из стека, нужно и презентер очистить
                    // т.к., у презентера свой фрагмент с instance retained
                    PersistentScreenScope.destroyImmediately((BaseActivity) getActivity(),
                            ((CoreFragmentView) fragment).getScreenConfigurator().getName());
                }

                return true;
            }
        }

        return false;
    }
}
