package ru.surfstudio.android.core.ui.base.event.delegate.newintent;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

/**
 * делегат, обрабатывающий событие onNewIntent
 */
public interface NewIntentDelegate extends ScreenEventDelegate {

    boolean onNewIntent(Intent intent);
}
