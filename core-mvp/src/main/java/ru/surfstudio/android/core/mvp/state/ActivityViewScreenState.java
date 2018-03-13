package ru.surfstudio.android.core.mvp.state;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;

/**
 * Предоставляет текущее состояние экрана и живую активити - вью
 */

public class ActivityViewScreenState extends ActivityScreenState {
    private CoreActivityViewInterface coreActivityView;

    public void onDestroy() {
        super.onDestroy();
        coreActivityView = null;
    }

    public void onCreate(FragmentActivity activity, CoreActivityViewInterface coreActivityView, @Nullable Bundle savedInstanceState) {
        super.onCreate(activity, coreActivityView, savedInstanceState);
        this.coreActivityView = coreActivityView;
    }

    @Override
    @Deprecated
    public void onCreate(FragmentActivity activity, CoreActivityInterface coreActivity, @Nullable Bundle savedInstanceState) {
        throw new UnsupportedOperationException("call another onCreate");
    }

    public CoreActivityViewInterface getCoreActivityView() {
        return coreActivityView;
    }
}
