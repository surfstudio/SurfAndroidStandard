package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.start;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnStartDelegate extends ScreenEventDelegate {

    void onStart();
}
