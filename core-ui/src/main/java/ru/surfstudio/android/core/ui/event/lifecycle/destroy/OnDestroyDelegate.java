package ru.surfstudio.android.core.ui.event.lifecycle.destroy;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие уничтожения обьекта экрана
 */
public interface OnDestroyDelegate extends ScreenEventDelegate {

    void onDestroy();
}
