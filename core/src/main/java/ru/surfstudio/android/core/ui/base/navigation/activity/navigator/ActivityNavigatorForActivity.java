package ru.surfstudio.android.core.ui.base.navigation.activity.navigator;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;


public class ActivityNavigatorForActivity extends ActivityNavigator {

    private ActivityProvider activityProvider;

    public ActivityNavigatorForActivity(ActivityProvider activityProvider,
                                        ScreenEventDelegateManager eventDelegateManager) {
        super(activityProvider, eventDelegateManager);
        this.activityProvider = activityProvider;
    }

    @Override
    protected void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle bundle) {
        activityProvider.get().startActivityForResult(intent, requestCode, bundle);
    }
}
