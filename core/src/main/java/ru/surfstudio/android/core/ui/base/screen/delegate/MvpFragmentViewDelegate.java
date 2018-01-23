package ru.surfstudio.android.core.ui.base.screen.delegate;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready.OnViewReadyEvent;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderFragmentCoreView;

public class MvpFragmentViewDelegate extends BaseFragmentDelegate {

    private Fragment fragment;
    private PresenterHolderFragmentCoreView view;

    public <F extends Fragment & PresenterHolderFragmentCoreView> MvpFragmentViewDelegate(
            F fragment,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers) {
        super(fragment, scopeStorage, eventResolvers);
        this.view = fragment;
        this.fragment = fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view.bindPresenters();
        view.onActivityCreated(savedInstanceState, getPersistentScope().isActivityRecreated());
        getEventDelegateManager().sendEvent(new OnViewReadyEvent());
    }

    @Override
    protected BaseFragmentScreenConfigurator createConfigurator() { //todo safe check
        BaseActivityConfigurator parentConfigurator = ((BaseActivityInterface) fragment.getActivity()).getBaseActivityDelegate().getConfigurator();
        return view.createScreenConfigurator(parentConfigurator, view.getStartArgs());
    }

    @Override
    public BaseFragmentScreenConfigurator getConfigurator() { //todo safe check
        return (BaseFragmentScreenConfigurator)super.getConfigurator();
    }
}
