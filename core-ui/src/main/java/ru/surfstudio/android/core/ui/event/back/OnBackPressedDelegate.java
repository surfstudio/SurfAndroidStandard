package ru.surfstudio.android.core.ui.event.back;


import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие активити OnBackPressed
 */
public interface OnBackPressedDelegate extends ScreenEventDelegate {

    boolean onBackPressed();
}
