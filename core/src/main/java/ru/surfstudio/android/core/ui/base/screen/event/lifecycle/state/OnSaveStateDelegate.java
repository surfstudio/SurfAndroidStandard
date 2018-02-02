package ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state;


import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие сохранения состояния
 */
public interface OnSaveStateDelegate extends ScreenEventDelegate {
    void onSaveState(Bundle outState);
}
