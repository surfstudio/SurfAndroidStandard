package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.pause;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onPause
 */
public interface OnPauseDelegate extends ScreenEventDelegate {

    void onPause();
}
