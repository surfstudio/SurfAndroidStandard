package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.stop;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnStopDelegate extends ScreenEventDelegate {

    void onStop();
}
