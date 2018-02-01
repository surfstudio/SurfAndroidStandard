package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.view.destroy;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие уничтожения иерархии вью экрана
 */
public interface OnViewDestroyDelegate extends ScreenEventDelegate {

    void onViewDestroy();
}
