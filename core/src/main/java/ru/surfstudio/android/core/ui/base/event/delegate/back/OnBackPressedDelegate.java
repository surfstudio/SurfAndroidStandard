package ru.surfstudio.android.core.ui.base.event.delegate.back;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onActivityResult
 */
public interface OnBackPressedDelegate extends ScreenEventDelegate {

    boolean onBackPressed();
}
