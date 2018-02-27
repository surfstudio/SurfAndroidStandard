package ru.surfstudio.android.core.ui.event.lifecycle.start;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnStartDelegate extends ScreenEventDelegate {

    void onStart();
}
