package ru.surfstudio.android.core.mvp.activity;

import ru.surfstudio.android.core.mvp.delegate.ActivityViewDelegate;
import ru.surfstudio.android.core.mvp.delegate.factory.MvpScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.mvp.presenter.CorePresenter;
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.ui.activity.CoreActivity;

/**
 * Base class with core logic for view, based on {@link CoreActivity}
 */
public abstract class CoreActivityView extends CoreActivity implements
        CoreActivityViewInterface {

    protected abstract CorePresenter[] getPresenters();

    public abstract String getScreenName();

    @Override
    public ActivityViewDelegate createActivityDelegate() {
        return MvpScreenDelegateFactoryContainer.get().createActivityViewDelegate(this);
    }

    @Override
    public ActivityViewPersistentScope getPersistentScope() {
        return (ActivityViewPersistentScope) super.getPersistentScope();
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
