package ru.surfstudio.android.core.ui.event.newintent;


import android.content.Intent;

import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onNewIntent
 */
public interface NewIntentDelegate extends ScreenEventDelegate {

    boolean onNewIntent(Intent intent);
}
