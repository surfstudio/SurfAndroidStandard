package ru.surfstudio.android.core.ui.base.screen.delegate;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.agna.ferro.core.PSSDelegate;
import com.agna.ferro.core.PersistentScreenScope;

import ru.surfstudio.android.core.app.log.LogConstants;
import ru.surfstudio.android.core.app.log.RemoteLogger;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

public abstract class MvpViewDelegate {

    interface PresenterEventDispatcher{
        void dispatch(CorePresenter presenter);
    }

    private Activity activity;
    private PresenterHolderCoreView view;

    private ScreenConfigurator screenConfigurator;

    public MvpViewDelegate(Activity activity, PresenterHolderCoreView view) {
        this.activity = activity;
        this.view = view;
    }

    protected abstract ScreenConfigurator createScreenConfigurator(Activity activity);

    protected PresenterHolderCoreView getView(){
        return view;
    }

    protected abstract PSSDelegate getPssDelegate();

    public ScreenConfigurator getScreenConfigurator() {
        return screenConfigurator;
    }

    public PersistentScreenScope getScreenScope() {
        return getPssDelegate().getScreenScope();
    }

    public void onPreMvpViewCreate() {
        //необходимо вызывать до super.onCreate, если делегат используется для активити,
        //поскольку создание screenConfigurator должно происходить раньше инициализации pssDelegate,
        // как раз инициализируется в super.onCreate - см BaseActivityDelegate
        screenConfigurator = createScreenConfigurator(activity);
    }

    public void onMvpViewCreate(Bundle savedInstanceState, PersistableBundle persistentState){
        initPssDelegate();
        screenConfigurator.setPersistentScreenScope(getPssDelegate().getScreenScope());
        onPreBindPresenters(savedInstanceState, persistentState, getPssDelegate().isScreenRecreated());
        screenConfigurator.satisfyDependencies(getView());
        registerPresentersAsScopeDestroyListeners();
        getView().bindPresenters();
        onPostBindPresenters(savedInstanceState, persistentState, getPssDelegate().isScreenRecreated());
        dispatchEventToPresenters(presenter -> presenter.onLoad(getPssDelegate().isScreenRecreated()));
        dispatchEventToPresenters(CorePresenter::onLoadFinished);
    }

    protected abstract void initPssDelegate();

    protected abstract void onPostBindPresenters(Bundle savedInstanceState, PersistableBundle persistentState, boolean screenRecreated);

    protected abstract void onPreBindPresenters(Bundle savedInstanceState, PersistableBundle persistentState, boolean screenRecreated);

    private void registerPresentersAsScopeDestroyListeners() {
        for (CorePresenter presenter : getView().getPresenters()) {
            if (!getPssDelegate().isScreenRecreated()) {
                getPssDelegate().getScreenScope().addOnScopeDestroyListener(presenter);
            }
        }
    }

    public void onStart() {
        dispatchEventToPresenters(CorePresenter::onStart);
    }

    public void onResume() {
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SCREEN_RESUME_FORMAT, screenConfigurator.getName()));
        dispatchEventToPresenters(CorePresenter::onResume);
    }

    public void onPause() {
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SCREEN_PAUSE_FORMAT, screenConfigurator.getName()));
        dispatchEventToPresenters(CorePresenter::onPause);
    }

    public void onStop() {
        dispatchEventToPresenters(CorePresenter::onStop);
    }

    private void dispatchEventToPresenters(PresenterEventDispatcher presenterEventDispatcher) {
        for (CorePresenter presenter : getView().getPresenters()) {
            presenterEventDispatcher.dispatch(presenter);
        }
    }

    public void onDestroyView() {
        dispatchEventToPresenters(CorePresenter::detachView);
    }

    public abstract void onDestroy();
}
