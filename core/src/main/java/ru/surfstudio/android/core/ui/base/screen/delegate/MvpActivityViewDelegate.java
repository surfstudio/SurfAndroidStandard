package ru.surfstudio.android.core.ui.base.screen.delegate;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready.OnViewReadyEvent;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;

public class MvpActivityViewDelegate extends BaseActivityDelegate {

    private Activity activity;
    private ActivityCoreView view;
    private ScreenConfigurator screenConfigurator;

    public <A extends FragmentActivity & BaseActivityInterface & ActivityCoreView> MvpActivityViewDelegate(A activity) {
        super(activity);
        this.activity = activity;
        this.view = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        view.onPreCreate(savedInstanceState, getPersistentScope().isActivityRecreated());
        view.setContentView(view.getContentView());
        super.onCreate(savedInstanceState);
        view.bindPresenters();
        view.onCreate(savedInstanceState, getPersistentScope().isActivityRecreated());
        getEventDelegateManager().sendEvent(new OnViewReadyEvent());
    }

    @Override
    protected void createConfigurators() {
        super.createConfigurators();
        screenConfigurator = view.createScreenConfigurator(activity, view.getStartIntent());
    }

    @Override
    protected void runConfigurators() {
        super.runConfigurators();
        screenConfigurator.satisfyDependencies(getPersistentScope(), view);
    }

    @Override
    protected String getName() {
        return screenConfigurator.getName();
    }
}
