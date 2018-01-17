package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.common;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnCreateDelegate extends ScreenEventDelegate {

    boolean onCreate();
}
