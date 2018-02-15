package ru.surfstudio.android.core.ui.base.screen.event.result;


import android.support.annotation.NonNull;

import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEvent;

/**
 * событие onRequestPermissionsResult
 */
public class RequestPermissionsResultEvent implements ScreenEvent {
    private int requestCode;
    @NonNull
    private String[] permissions;
    @NonNull
    private int[] grantResults;

    public RequestPermissionsResultEvent(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }

    public int getRequestCode() {
        return requestCode;
    }

    @NonNull
    public String[] getPermissions() {
        return permissions;
    }

    @NonNull
    public int[] getGrantResults() {
        return grantResults;
    }

}
