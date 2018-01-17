package ru.surfstudio.android.core.ui.base.event.delegate.newintent;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;

public class NewIntentEvent implements ScreenEvent {
    private Intent intent;

    public NewIntentEvent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }
}
