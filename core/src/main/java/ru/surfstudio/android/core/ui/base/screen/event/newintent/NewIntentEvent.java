package ru.surfstudio.android.core.ui.base.screen.event.newintent;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEvent;

/**
 *  событие onNewIntent
 */
public class NewIntentEvent implements ScreenEvent {
    private Intent intent;

    public NewIntentEvent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }
}
