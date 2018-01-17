package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state;


import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 * todo обьединить с OnSavesStateDel
 */
public interface OnRestoreStateDelegate extends ScreenEventDelegate {

    void onRestoreState(@Nullable Bundle savedInstanceState);
}
