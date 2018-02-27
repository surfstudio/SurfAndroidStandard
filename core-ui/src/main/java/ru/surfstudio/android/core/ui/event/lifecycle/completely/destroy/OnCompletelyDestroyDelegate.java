package ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие полного уничтожения экрана
 */
public interface OnCompletelyDestroyDelegate extends ScreenEventDelegate {

    void onCompletelyDestroy();
}
