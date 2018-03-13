package ru.surfstudio.android.core.ui.event.lifecycle.state;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие сохранения состояния
 */
public interface OnSaveStateDelegate extends ScreenEventDelegate {
    void onSaveState(Bundle outState);
}
