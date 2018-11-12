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
package ru.surfstudio.android.core.ui.state;

import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * базовый {@link ScreenState} для активити и фрагмента, для виджета свой ScreenState
 */

public abstract class BaseScreenState implements ScreenState {
    private boolean viewRecreated = false;
    private boolean screenRecreated = false;
    private boolean completelyDestroyed = false;

    private boolean restoredFromDisk = false;

    private boolean viewDestroyedAtListOnce = false;
    private boolean screenDestroyedAtListOnce = false;

    private ScreenStates currentState;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        viewRecreated = viewDestroyedAtListOnce;
        screenRecreated = screenDestroyedAtListOnce;
        restoredFromDisk = restoredFromDisk ||
                !screenDestroyedAtListOnce && savedInstanceState != null;

        currentState = ScreenStates.CREATED;
    }

    public void onViewReady() {
        currentState = ScreenStates.VIEW_READY;
    }

    public void onStart() {
        currentState = ScreenStates.STARTED;
    }

    public void onResume() {
        currentState = ScreenStates.RESUMED;
    }

    public void onPause() {
        currentState = ScreenStates.PAUSED;
    }

    public void onStop() {
        currentState = ScreenStates.STOPPED;
    }

    public void onDestroy() {
        screenDestroyedAtListOnce = true;
    }

    public void onDestroyView() {
        currentState = ScreenStates.VIEW_DESTROYED;
        viewDestroyedAtListOnce = true;
    }

    public void onCompletelyDestroy() {
        currentState = ScreenStates.DESTROYED;
        completelyDestroyed = true;
    }

    @Override
    public boolean isViewRecreated() {
        return viewRecreated;
    }

    @Override
    public boolean isScreenRecreated() {
        return screenRecreated;
    }

    @Override
    public boolean isCompletelyDestroyed() {
        return completelyDestroyed;
    }

    @Override
    public boolean isRestoredFromDisk() {
        return restoredFromDisk;
    }

    @Override
    public boolean isRestoredFromDiskJustNow() {
        return isRestoredFromDisk() && !isViewRecreated();
    }

    @Override
    public ScreenStates getCurrentState() {
        return currentState;
    }
}
