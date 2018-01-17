package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.fragment;


import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnCreateFragmentDelegate extends ScreenEventDelegate {

    boolean onCreate(Fragment fragment);
}
