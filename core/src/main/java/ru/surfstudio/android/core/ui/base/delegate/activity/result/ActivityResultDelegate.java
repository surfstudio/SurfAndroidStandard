package ru.surfstudio.android.core.ui.base.delegate.activity.result;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.delegate.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface ActivityResultDelegate extends ScreenEventDelegate {

    boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
