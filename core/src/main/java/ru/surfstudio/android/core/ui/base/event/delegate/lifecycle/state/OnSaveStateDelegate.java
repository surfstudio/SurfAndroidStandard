package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnSaveStateDelegate extends ScreenEventDelegate {
    void onSaveState(Bundle outState);
}
