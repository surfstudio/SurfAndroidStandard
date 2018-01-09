package ru.surfstudio.android.core.ui.base.screen.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.R;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.MvpActivityViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.MvpViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderActivityCoreView;

/**
 * Base class with core logic for view, based on Activity
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class CoreActivityView extends BaseActivity implements
        PresenterHolderActivityCoreView {


    private MvpViewDelegate viewDelegate;

    /**
     * Override this instead {@link #onCreate(Bundle)}
     *
     * @param viewRecreated render whether view created in first time or recreated after
     *                      changing configuration
     */
    @Override
    public void onCreate(Bundle savedInstanceState, @Nullable PersistableBundle persistentState, boolean viewRecreated) {
        if(getResources().getBoolean(R.bool.only_portrait)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    /**
     * Called before Presenter is bound to the View and content view is created
     *
     * @param savedInstanceState
     * @param viewRecreated
     */
    @Override
    public void onPreCreate(Bundle savedInstanceState, @Nullable PersistableBundle persistentState, boolean viewRecreated) {

    }


    @Override
    public final void onCreate(Bundle savedInstanceState) {
        viewDelegate = new MvpActivityViewDelegate(this, this, this);
        viewDelegate.onPreMvpViewCreate();
        super.onCreate(savedInstanceState);
        viewDelegate.onMvpViewCreate(savedInstanceState, null);
    }

    @Override
    public ScreenConfigurator getScreenConfigurator() {
        return viewDelegate.getScreenConfigurator();
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

    @Override
    public String getName() {
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
    public void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroyView();
        viewDelegate.onDestroy();
    }
}
