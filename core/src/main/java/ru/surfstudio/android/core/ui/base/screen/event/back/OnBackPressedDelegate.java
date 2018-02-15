package ru.surfstudio.android.core.ui.base.screen.event.back;


import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие активити OnBackPressed
 */
public interface OnBackPressedDelegate extends ScreenEventDelegate {

    boolean onBackPressed();
}
