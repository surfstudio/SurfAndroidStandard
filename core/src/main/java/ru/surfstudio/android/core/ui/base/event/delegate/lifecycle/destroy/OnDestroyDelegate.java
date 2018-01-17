package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnDestroyDelegate extends ScreenEventDelegate {

    void onDestroy();
}
