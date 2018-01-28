package ru.surfstudio.android.core.ui.base.screen.delegate.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;

public class FragmentViewDelegate extends FragmentDelegate {

    private CoreFragmentViewInterface coreFragmentView;

    public <F extends Fragment & CoreFragmentViewInterface> FragmentViewDelegate(
            F fragment,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            FragmentCompletelyDestroyChecker completelyDestroyChecker) {
        super(fragment, scopeStorage, eventResolvers, completelyDestroyChecker);
        this.coreFragmentView = fragment;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState) {
        coreFragmentView.bindPresenters();
        super.prepareView(savedInstanceState);
    }

    @Override
    protected BaseFragmentViewConfigurator createConfigurator() {
        return coreFragmentView.createConfigurator();
    }

    @Override
    public BaseFragmentViewConfigurator getConfigurator() {
        return (BaseFragmentViewConfigurator) super.getConfigurator();
    }
}
