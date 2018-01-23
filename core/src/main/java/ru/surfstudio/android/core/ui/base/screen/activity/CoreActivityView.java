package ru.surfstudio.android.core.ui.base.screen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderActivityCoreView;

/**
 * Base class with core logic for view, based on Activity
 */
public abstract class CoreActivityView extends BaseActivity implements
        PresenterHolderActivityCoreView {


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
     * Called before Presenter is bound to the View and content view is created
     *
     * @param savedInstanceState
     * @param viewRecreated
     */
    @Override
    public void onPreCreate(Bundle savedInstanceState, boolean viewRecreated) {

    }


    /**
     * A wrapper method for internal use
     * Routed to {@link BaseActivityScreenConfigurator}
     *
     * @return the intent the activity started with
     * @see Activity#getIntent()
     */
    @Override
    public final Intent getStartIntent() {
        return getIntent();
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
