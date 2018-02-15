package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.destroy;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие уничтожения обьекта экрана
 */
public interface OnDestroyDelegate extends ScreenEventDelegate {

    void onDestroy();
}
