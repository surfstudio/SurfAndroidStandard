package ru.surfstudio.android.core.ui.base.screen.state;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by makstuev on 25.01.2018.
 */

public abstract class ScreenState { //todo описать что флаги для смены конфигурации
    private boolean viewRecreated = false;
    private boolean screenRecreated = false;
    private boolean completelyDestroyed = false;

    private boolean restoredFromDisk = false;

    private boolean viewDestroyedAtListOnce = false;
    private boolean screenDestroyedAtListOnce = false;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        viewRecreated = viewDestroyedAtListOnce;
        screenRecreated = screenDestroyedAtListOnce;
        restoredFromDisk = !screenDestroyedAtListOnce && savedInstanceState != null;
    }

    public void onDestroyView() {
        screenDestroyedAtListOnce = true;
    }

    public void onDestroy() {
        screenDestroyedAtListOnce = true;
    }

    public void onCompletelyDestroy() {
        completelyDestroyed = true;
    }

    public boolean isViewRecreated() {
        return viewRecreated;
    }

    public boolean isScreenRecreated() {
        return screenRecreated;
    }

    public boolean isCompletelyDestroyed() {
        return completelyDestroyed;
    }

    public boolean isRestoredFromDisk() {
        return restoredFromDisk;
    }
}
