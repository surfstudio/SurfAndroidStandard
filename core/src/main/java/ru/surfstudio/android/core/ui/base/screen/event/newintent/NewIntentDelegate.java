package ru.surfstudio.android.core.ui.base.screen.event.newintent;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onNewIntent
 */
public interface NewIntentDelegate extends ScreenEventDelegate {

    boolean onNewIntent(Intent intent);
}
