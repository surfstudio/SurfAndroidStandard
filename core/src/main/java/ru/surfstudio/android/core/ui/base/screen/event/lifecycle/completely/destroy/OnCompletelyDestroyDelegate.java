package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.completely.destroy;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие полного уничтожения экрана
 */
public interface OnCompletelyDestroyDelegate extends ScreenEventDelegate {

    void onCompletelyDestroy();
}
