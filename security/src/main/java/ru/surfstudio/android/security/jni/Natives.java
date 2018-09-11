package ru.surfstudio.android.security.jni;

import ru.surfstudio.android.security.root.error.RootDetectedException;

/**
 * JNI мост, при инициализации проходит проверка на root-права
 */
public class Natives {

    static {
        System.loadLibrary("native_jni");
    }

    private Natives() {

    }

    public static native void init() throws RootDetectedException;
}
