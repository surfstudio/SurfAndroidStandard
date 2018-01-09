package ru.surfstudio.android.core.ui.base.screen.delegate;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import com.agna.ferro.core.PSSDelegate;

import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderActivityCoreView;

public class MvpActivityViewDelegate extends MvpViewDelegate {

    private BaseActivityInterface baseActivity;

    public MvpActivityViewDelegate(FragmentActivity activity,
                                   PresenterHolderActivityCoreView view,
                                   BaseActivityInterface baseActivityInterface) {
        super(activity, view);
        this.baseActivity = baseActivityInterface;
    }

    @Override
    protected ScreenConfigurator createScreenConfigurator(Activity activity) {
        return getView().createScreenConfigurator(activity, getView().getStartIntent());
    }

    @Override
    protected PresenterHolderActivityCoreView getView() {
        return (PresenterHolderActivityCoreView) super.getView();
    }

    @Override
    protected PSSDelegate getPssDelegate() {
        return baseActivity.getBaseActivityDelegate().getPssDelegate();
    }

    @Override
    protected void initPssDelegate() {
        //empty т.к уже проинициализировано в BaseActivityDelegate
    }

    @Override
    protected void onPostBindPresenters(Bundle savedInstanceState, PersistableBundle persistentState, boolean screenRecreated) {
        getView().onCreate(savedInstanceState, persistentState, screenRecreated);
    }

    @Override
    protected void onPreBindPresenters(Bundle savedInstanceState, PersistableBundle persistentState, boolean screenRecreated) {
        getView().onPreCreate(savedInstanceState, persistentState, screenRecreated);
        getView().setContentView(getView().getContentView());
    }

    public void onDestroy() {
        //empty т.к уже обработано в BaseActivityDelegate
    }

}
