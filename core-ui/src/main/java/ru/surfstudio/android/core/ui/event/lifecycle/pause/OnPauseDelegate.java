package ru.surfstudio.android.core.ui.event.lifecycle.pause;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onPause
 */
public interface OnPauseDelegate extends ScreenEventDelegate {

    void onPause();
}
