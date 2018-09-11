package ru.surfstudio.android.security.root.error;

import android.support.annotation.Keep;

@Keep
public class RootDetectedException extends Exception {
    public RootDetectedException(String message) {
        super(message);
    }
}
