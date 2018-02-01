package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.resume;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnResumeDelegate extends ScreenEventDelegate {

    void onResume();
}
