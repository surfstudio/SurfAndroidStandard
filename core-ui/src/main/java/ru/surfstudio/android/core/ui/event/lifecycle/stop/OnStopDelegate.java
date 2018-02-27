package ru.surfstudio.android.core.ui.event.lifecycle.stop;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnStopDelegate extends ScreenEventDelegate {

    void onStop();
}
