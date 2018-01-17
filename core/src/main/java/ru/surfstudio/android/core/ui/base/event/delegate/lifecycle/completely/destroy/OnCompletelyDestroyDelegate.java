package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.completely.destroy;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnCompletelyDestroyDelegate extends ScreenEventDelegate {

    void onCompletelyDestroy();
}
