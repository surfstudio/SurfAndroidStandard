package ru.surfstudio.android.core.ui.base.screen.delegate;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.agna.ferro.core.PSSDelegate;
import com.agna.ferro.core.PSSFragmentDelegate;

import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderFragmentCoreView;

public class MvpFragmentViewDelegate extends MvpViewDelegate {

    private final Fragment fragment;
    private final PresenterHolderFragmentCoreView view;
    private PSSDelegate pssDelegate;

    public MvpFragmentViewDelegate(Activity activity, Fragment fragmentView, PresenterHolderFragmentCoreView view) {
        super(activity, view);
        fragment = fragmentView;
        this.view = view;
    }


    @Override
    protected PSSDelegate getPssDelegate() {
        return pssDelegate;
    }

    @Override
    protected ScreenConfigurator createScreenConfigurator(Activity activity) {
        return getView().createScreenConfigurator(activity, getView().getStartArgs());
    }

    @Override
    protected PresenterHolderFragmentCoreView getView() {
        return (PresenterHolderFragmentCoreView) super.getView();
    }

    @Override
    protected void initPssDelegate() {
        pssDelegate = new PSSFragmentDelegate(view, fragment);
        pssDelegate.init();
    }

    @Override
    protected void onPostBindPresenters(Bundle savedInstanceState, PersistableBundle persistentState, boolean screenRecreated) {
        getView().onActivityCreated(savedInstanceState, screenRecreated);
    }

    @Override
    protected void onPreBindPresenters(Bundle savedInstanceState, PersistableBundle persistentState, boolean screenRecreated) {
        //empty
    }

    public void onDestroy() {
        getPssDelegate().onDestroy();
    }

}
