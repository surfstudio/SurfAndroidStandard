package ru.surfstudio.android.core.ui.base.screen.delegate;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready.OnViewReadyEvent;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityScreenConfigurator;

public class MvpActivityViewDelegate extends BaseActivityDelegate { //todo именование BaseActivityScreenConfigurator vs MvpActivtyViewDelegate

    private FragmentActivity activity;
    private ActivityCoreView view;

    public <A extends FragmentActivity & BaseActivityInterface & ActivityCoreView> MvpActivityViewDelegate(
            A activity,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers) {
        super(activity, scopeStorage, eventResolvers);
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
    protected BaseActivityScreenConfigurator createConfigurator() {
        return view.createScreenConfigurator(activity, view.getStartIntent());
    }

    @Override
    public BaseActivityScreenConfigurator getConfigurator() { //todo safe check
        return (BaseActivityScreenConfigurator)super.getConfigurator();
    }


}
