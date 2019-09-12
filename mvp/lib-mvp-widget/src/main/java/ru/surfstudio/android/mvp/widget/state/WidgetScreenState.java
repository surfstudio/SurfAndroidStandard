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
import ru.surfstudio.android.core.ui.state.LifecycleStage;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * {@link ScreenState} для кастомной вью с презентером
 * Использует также родительский стейт
 * <p>
 */

public class WidgetScreenState implements ScreenState {

    private View widget;
    private CoreWidgetViewInterface coreWidget;

    private ScreenType parentType;
    private ScreenState parentState;
    private LifecycleStage lifecycleStage;
    private boolean isViewDestroyedAtLeastOnce = false;

    public WidgetScreenState(ScreenState parentState) {
        this.parentState = parentState;
        this.parentType = parentState instanceof ActivityScreenState
                ? ScreenType.ACTIVITY
                : ScreenType.FRAGMENT;
    }

    public void onCreate(View widget, CoreWidgetViewInterface coreWidget) {
        this.widget = widget;
        this.coreWidget = coreWidget;

        lifecycleStage = LifecycleStage.CREATED;
    }

    public void onViewReady() {
        lifecycleStage = LifecycleStage.VIEW_CREATED;
    }

    public void onStart() {
        lifecycleStage = LifecycleStage.STARTED;
    }

    public void onResume() {
        lifecycleStage = LifecycleStage.RESUMED;
    }

    public void onPause() {
        lifecycleStage = LifecycleStage.PAUSED;
    }

    public void onStop() {
        lifecycleStage = LifecycleStage.STOPPED;
    }

    public void onViewDestroy() {
        lifecycleStage = LifecycleStage.VIEW_DESTROYED;
        isViewDestroyedAtLeastOnce = true;
    }

    public void onCompletelyDestroy() {
        lifecycleStage = LifecycleStage.COMPLETELY_DESTROYED;
        this.widget = null;
        this.coreWidget = null;
    }

    @Override
    public boolean isViewRecreated() {
        return parentState.isViewRecreated() || isViewDestroyedAtLeastOnce;
    }

    @Override
    public boolean isScreenRecreated() {
        return this.isViewRecreated();
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
    public LifecycleStage getLifecycleStage() {
        return lifecycleStage;
    }
}
