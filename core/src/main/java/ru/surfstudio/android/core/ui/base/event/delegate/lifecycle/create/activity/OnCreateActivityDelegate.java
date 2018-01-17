package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.activity;


import android.app.Activity;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnCreateActivityDelegate extends ScreenEventDelegate {

    void onCreate(Activity activity);
}
