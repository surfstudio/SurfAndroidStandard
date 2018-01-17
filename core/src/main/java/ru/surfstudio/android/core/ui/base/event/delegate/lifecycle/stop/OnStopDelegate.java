package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.stop;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnStopDelegate extends ScreenEventDelegate {

    void onStop();
}
