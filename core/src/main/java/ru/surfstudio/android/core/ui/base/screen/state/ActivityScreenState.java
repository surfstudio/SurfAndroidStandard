package ru.surfstudio.android.core.ui.base.screen.state;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by makstuev on 25.01.2018.
 */

public class ActivityScreenState extends ScreenState {
    private Activity activity;

    public void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    public void onCreate(Activity activity, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
