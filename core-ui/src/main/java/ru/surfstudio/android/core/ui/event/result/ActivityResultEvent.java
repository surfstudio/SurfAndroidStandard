package ru.surfstudio.android.core.ui.event.result;


import android.content.Intent;

import ru.surfstudio.android.core.ui.event.base.ScreenEvent;

/**
 * событие onActivityResult
 */
public class ActivityResultEvent implements ScreenEvent {
    private int requestCode;
    private int resultCode;
    private Intent data;

    public ActivityResultEvent(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public Intent getData() {
        return data;
    }
}
