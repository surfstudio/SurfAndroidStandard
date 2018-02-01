package ru.surfstudio.android.core.ui.base.screen.presenter;


import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.pause.OnPauseDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.ready.OnViewReadyDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.resume.OnResumeDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.start.OnStartDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state.OnRestoreStateDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state.OnSaveStateDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.stop.OnStopDelegate;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.view.destroy.OnViewDestroyDelegate;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;


public class CorePresenterGateway implements
        OnViewReadyDelegate,
        OnStartDelegate,
        OnResumeDelegate,
        OnPauseDelegate,
        OnStopDelegate,
        OnViewDestroyDelegate,
        OnCompletelyDestroyDelegate,
        OnSaveStateDelegate,
        OnRestoreStateDelegate {

    private static final String KEY_STATE = "KEY_STATE";
    private CorePresenter presenter;
    private ScreenState screenState;

    public CorePresenterGateway(CorePresenter presenter,
                                ScreenState screenState) {
        this.presenter = presenter;
        this.screenState = screenState;
    }


    @Override
    public void onStop() {
        presenter.onStop();
    }

    @Override
    public void onPause() {
        presenter.onPause();
    }

    @Override
    public void onStart() {
        presenter.onStart();
    }

    @Override
    public void onResume() {
        presenter.onResume();
    }

    @Override
    public void onViewReady() {
        if (!screenState.isViewRecreated()) {
            presenter.onFirstLoad();
        }
        presenter.onLoad(screenState.isViewRecreated());
    }

    @Override
    public void onViewDestroy() {
        presenter.onViewDetached();
    }

    @Override
    public void onCompletelyDestroy() {
        presenter.onDestroy();
    }

    @Override
    public void onSaveState(Bundle outState) {
        if (presenter.getStateRestorer() != null) {
            outState.putSerializable(KEY_STATE, presenter.getStateRestorer().getCurrentState());
        }
    }

    @Override
    public void onRestoreState(@Nullable Bundle savedInstanceState) {
        if (presenter.getStateRestorer() != null && savedInstanceState!= null) {
            presenter.getStateRestorer().restoreState(savedInstanceState.getSerializable(KEY_STATE));
        }
    }
}
