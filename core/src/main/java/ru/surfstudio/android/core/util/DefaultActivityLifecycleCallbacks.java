package ru.surfstudio.android.core.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Слушатель жизненного цикла автикити по умолчанию
 */
public class DefaultActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //do nothing
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        //do nothing
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //do nothing
    }
}
