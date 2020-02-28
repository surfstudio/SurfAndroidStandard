/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.mvp.presenter;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyDelegate;
import ru.surfstudio.android.core.ui.state.ScreenState;

/**
 * Оповещает презентер о событиях экрана
 * Сохраняет и восстанавливант состояние через Bundle, см {@link StateRestorer}
 */
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
        presenter.detachView();
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
        if (presenter.getStateRestorer() != null
                && screenState.isRestoredFromDiskJustNow()) {
            //восстанавливаем состояние только если экран восстановлен с диска
            // и этот обработчик не был вызван после смены конфигурации
            presenter.getStateRestorer().restoreState(savedInstanceState.getSerializable(KEY_STATE));
        }
    }
}
