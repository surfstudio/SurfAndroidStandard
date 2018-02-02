package ru.surfstudio.android.core.ui.base.screen.event.result;


import android.support.annotation.NonNull;

import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onRequestPermissionsResult
 */
public interface RequestPermissionsResultDelegate extends ScreenEventDelegate {

    boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
