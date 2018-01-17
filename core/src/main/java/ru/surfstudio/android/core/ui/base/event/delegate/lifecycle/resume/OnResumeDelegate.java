package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.resume;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnResumeDelegate extends ScreenEventDelegate {

    void onResume();
}
