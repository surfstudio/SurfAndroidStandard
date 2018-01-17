package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.pause;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnPauseDelegate extends ScreenEventDelegate {

    void onPause();
}
