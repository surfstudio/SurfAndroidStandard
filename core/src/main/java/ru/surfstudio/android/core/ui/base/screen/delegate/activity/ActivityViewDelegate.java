package ru.surfstudio.android.core.ui.base.screen.delegate.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;

/**
 * делегат для активити вью, кроме логики базового делегата добавляет управление предентерами
 */
public class ActivityViewDelegate extends ActivityDelegate {

    private CoreActivityViewInterface coreActivityView;

    public <A extends FragmentActivity & CoreActivityViewInterface> ActivityViewDelegate(
            A activity,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            ActivityCompletelyDestroyChecker completelyDestroyChecker) {
        super(activity, scopeStorage, eventResolvers, completelyDestroyChecker);
        this.coreActivityView = activity;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistableBundle) {
        coreActivityView.bindPresenters();
        super.prepareView(savedInstanceState, persistableBundle);
    }

    @Override
    protected BaseActivityViewConfigurator createConfigurator() {
        return coreActivityView.createConfigurator();
    }

    @Override
    public BaseActivityViewConfigurator getConfigurator() {
        return (BaseActivityViewConfigurator) super.getConfigurator();
    }
}
