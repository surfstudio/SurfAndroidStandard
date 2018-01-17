package ru.surfstudio.android.core.ui.base.screen.delegate;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready.OnViewReadyEvent;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.BaseFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.view.core.FragmentCoreView;

public class MvpFragmentViewDelegate extends BaseFragmentDelegate {

    private FragmentCoreView view;
    private BaseActivityInterface baseActivity;
    private ScreenConfigurator screenConfigurator;

    public <F extends Fragment & BaseFragmentInterface & FragmentCoreView> MvpFragmentViewDelegate(F fragment, BaseActivityInterface baseActivity) {
        super(fragment);
        this.view = fragment;
        this.baseActivity = baseActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view.bindPresenters();
        view.onActivityCreated(savedInstanceState, getPersistentScope().isActivityRecreated());
        getEventDelegateManager().sendEvent(new OnViewReadyEvent());
    }

    @Override
    protected void createConfigurators() {
        super.createConfigurators();
        screenConfigurator = view.createScreenConfigurator(baseActivity, view.getStartArgs());
    }

    @Override
    protected void runConfigurators() {
        super.runConfigurators();
        screenConfigurator.satisfyDependencies(getPersistentScope(), view);
    }

    @Override
    public String getName() {
        return screenConfigurator.getName();
    }
}
