package ru.surfstudio.android.core.ui.base.screen.delegate.activity;

import android.app.Activity;

import ru.surfstudio.android.core.ui.base.screen.delegate.base.CompletelyDestroyChecker;

/**
 * Created by makstuev on 25.01.2018.
 */

public class ActivityCompletelyDestroyChecker implements CompletelyDestroyChecker {
    private Activity activity;

    public ActivityCompletelyDestroyChecker(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean check() {
        return activity.isFinishing();
    }
}
