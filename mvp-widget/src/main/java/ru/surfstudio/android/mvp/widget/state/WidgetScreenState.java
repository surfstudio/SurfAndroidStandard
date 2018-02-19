package ru.surfstudio.android.mvp.widget.state;


import android.view.View;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * {@link ScreenState} для кастомной вью с презентером
 * Паразитирует на ScreenState родительской активити или фрагмента
 */

public class WidgetScreenState implements ScreenState {
    private View widget;
    private CoreWidgetViewInterface coreWidget;
    private ScreenType parentType;
    private ScreenState parentState;

    public WidgetScreenState(ScreenState parentState) {
        this.parentState = parentState;
        this.parentType = parentState instanceof ActivityScreenState
                ? ScreenType.ACTIVITY
                : ScreenType.FRAGMENT;
    }

    public void onCreate(View widget, CoreWidgetViewInterface coreWidget) {
        this.widget = widget;
        this.coreWidget = coreWidget;
    }

    public void onDestroy() {
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
}
