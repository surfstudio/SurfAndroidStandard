package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.start;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnStartDelegate extends ScreenEventDelegate {

    void onStart();
}
