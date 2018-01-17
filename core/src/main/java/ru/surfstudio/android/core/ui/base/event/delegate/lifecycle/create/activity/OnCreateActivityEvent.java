package ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.activity;


import android.app.Activity;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;

public class OnCreateActivityEvent implements ScreenEvent {
    private Activity activity;

    public OnCreateActivityEvent(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
