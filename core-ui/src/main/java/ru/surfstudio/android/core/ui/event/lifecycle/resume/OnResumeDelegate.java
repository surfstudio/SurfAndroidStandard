package ru.surfstudio.android.core.ui.event.lifecycle.resume;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnResumeDelegate extends ScreenEventDelegate {

    void onResume();
}
