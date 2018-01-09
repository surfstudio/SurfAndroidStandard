package ru.surfstudio.android.core.ui.base.delegate;


import android.content.Intent;

/**
 * делегат, обрабатывающий событие onNewIntent
 */
public interface NewIntentDelegate extends ScreenEventDelegate {

    boolean onNewIntent(Intent intent);
}
