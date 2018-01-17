package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnViewReadyDelegate extends ScreenEventDelegate {

    void onViewReady();
}
