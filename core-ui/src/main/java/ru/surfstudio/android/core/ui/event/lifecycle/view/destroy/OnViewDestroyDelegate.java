package ru.surfstudio.android.core.ui.event.lifecycle.view.destroy;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие уничтожения иерархии вью экрана
 */
public interface OnViewDestroyDelegate extends ScreenEventDelegate {

    void onViewDestroy();
}
