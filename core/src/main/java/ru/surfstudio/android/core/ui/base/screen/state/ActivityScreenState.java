package ru.surfstudio.android.core.ui.base.screen.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityInterface;

/**
 * Предоставляет текущее состояние экрана и живую активити - контейнер
 */

public class ActivityScreenState extends BaseScreenState {
    private FragmentActivity activity;
    private CoreActivityInterface coreActivity;

    public void onDestroy() {
        super.onDestroy();
        activity = null;
        coreActivity = null;
    }

    public void onCreate(FragmentActivity activity, CoreActivityInterface coreActivity,  @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = activity;
        this.coreActivity = coreActivity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public CoreActivityInterface getCoreActivity() {
        return coreActivity;
    }
}
