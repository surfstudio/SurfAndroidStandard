package ru.surfstudio.android.core.ui.base.navigation.activity.navigator;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;


public class ActivityNavigatorForActivity extends ActivityNavigator {

    private ActivityProvider activityProvider;

    public ActivityNavigatorForActivity(ActivityProvider activityProvider) {
        super(activityProvider);
        this.activityProvider = activityProvider;
    }

    @Override
    protected void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle bundle) {
        activityProvider.get().startActivityForResult(intent, requestCode, bundle);
    }
}
