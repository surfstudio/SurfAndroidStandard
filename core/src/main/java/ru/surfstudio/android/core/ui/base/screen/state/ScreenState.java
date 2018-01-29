package ru.surfstudio.android.core.ui.base.screen.state;

/**
 * Created by makstuev on 28.01.2018.
 */

public interface ScreenState {
    boolean isViewRecreated();

    boolean isScreenRecreated();

    boolean isCompletelyDestroyed();

    boolean isRestoredFromDisk();
}
