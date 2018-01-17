package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.view.destroy;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onViewDestroy
 */
public interface OnViewDestroyDelegate extends ScreenEventDelegate {

    void onViewDestroy();
}
