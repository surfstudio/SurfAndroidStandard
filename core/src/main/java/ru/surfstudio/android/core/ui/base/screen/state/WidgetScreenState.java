package ru.surfstudio.android.core.ui.base.screen.state;


import android.view.View;

import ru.surfstudio.android.core.ui.ScreenType;

/**
 * {@link ScreenState} для кастомной вью с презентером
 * Паразитирует на ScreenState родительской активити или фрагмента
 */

public class WidgetScreenState implements ScreenState {
    private View widget;
    private ScreenType parentType;
    private ScreenState parentState;

    public WidgetScreenState(ScreenState parentState) {
        this.parentState = parentState;
        this.parentType = parentState instanceof ActivityScreenState
                ? ScreenType.ACTIVITY
                : ScreenType.FRAGMENT;
    }

    public void onCreate(View widget) {
        this.widget = widget;
    }

    public void onDestroy() {
        this.widget = null;
    }

    public View getView() {
        return widget;
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

    public ScreenType getParentType() {
        return parentType;
    }

    public ScreenState getParentState() {
        return parentState;
    }

    public View getWidget() {
        return widget;
    }
}
