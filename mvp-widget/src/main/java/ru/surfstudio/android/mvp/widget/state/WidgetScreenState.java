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
package ru.surfstudio.android.mvp.widget.state;


import android.view.View;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.core.ui.state.ScreenStates;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * {@link ScreenState} для кастомной вью с презентером
 * Паразитирует на ScreenState родительской активити или фрагмента
 * <p>
 */

public class WidgetScreenState implements ScreenState {

    private View widget;
    private CoreWidgetViewInterface coreWidget;

    private ScreenType parentType;
    private ScreenState parentState;
    private ScreenStates currentState;

    public WidgetScreenState(ScreenState parentState) {
        this.parentState = parentState;
        this.parentType = parentState instanceof ActivityScreenState
                ? ScreenType.ACTIVITY
                : ScreenType.FRAGMENT;
    }

    public void onCreate(View widget, CoreWidgetViewInterface coreWidget) {
        this.widget = widget;
        this.coreWidget = coreWidget;

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

    public void onViewDestroy() {
        currentState = ScreenStates.VIEW_DESTROYED;
    }

    public void onDestroy() {
        currentState = ScreenStates.DESTROYED;
        this.widget = null;
        this.coreWidget = null;
    }

    @Override
    public boolean isViewRecreated() {
        return parentState.isViewRecreated();
    }

    @Override
    public boolean isScreenRecreated() {
        return parentState.isScreenRecreated();
    }

    @Override
    public boolean isCompletelyDestroyed() {
        return parentState.isCompletelyDestroyed();
    }

    @Override
    public boolean isRestoredFromDisk() {
        return parentState.isRestoredFromDisk();
    }

    @Override
    public boolean isRestoredFromDiskJustNow() {
        return parentState.isRestoredFromDiskJustNow();
    }

    public ScreenType getParentType() {
        return parentType;
    }

    public ScreenState getParentState() {
        return parentState;
    }

    public View getWidget() {
        return widget;
    }

    public CoreWidgetViewInterface getCoreWidget() {
        return coreWidget;
    }

    @Override
    public ScreenStates getCurrentState() {
        return currentState;
    }
}
