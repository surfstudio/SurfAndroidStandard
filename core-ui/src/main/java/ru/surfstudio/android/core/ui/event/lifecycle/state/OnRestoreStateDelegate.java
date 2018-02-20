package ru.surfstudio.android.core.ui.event.lifecycle.state;


import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyEvent;

/**
 * делегат, обрабатывающий событие восстановления состояния
 * вызовется до события {@link OnViewReadyEvent}
 */
public interface OnRestoreStateDelegate extends ScreenEventDelegate {

    void onRestoreState(@Nullable Bundle savedInstanceState);
}
