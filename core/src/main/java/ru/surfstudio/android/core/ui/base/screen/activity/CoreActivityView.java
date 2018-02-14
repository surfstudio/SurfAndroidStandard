package ru.surfstudio.android.core.ui.base.screen.activity;

import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityViewPersistentScope;

/**
 * Base class with core logic for view, based on {@link CoreActivity}
 */
public abstract class CoreActivityView extends CoreActivity implements
        CoreActivityViewInterface {

    protected abstract CorePresenter[] getPresenters();

    @Override
    public abstract String getName();

    @Override
    public ActivityViewDelegate createActivityDelegate() {
        return ScreenDelegateFactoryContainer.get().createActivityViewDelegate(this);
    }

    @Override
    public ActivityViewPersistentScope getPersistentScope() {
        return (ActivityViewPersistentScope)super.getPersistentScope();
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
