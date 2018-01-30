package ru.surfstudio.android.core.ui.base.permission;


import android.support.v4.app.ActivityCompat;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;

public class PermissionManagerForActivity extends PermissionManager {

    private ActivityProvider activityProvider;

    public PermissionManagerForActivity(ActivityProvider activityProvider,
                                        ScreenEventDelegateManager eventDelegateManager) {
        super(activityProvider, eventDelegateManager);
        this.activityProvider = activityProvider;
    }

    @Override
    protected void requestPermission(PermissionRequest request) {
        ActivityCompat.requestPermissions(activityProvider.get(), request.getPermissions(),
                request.getRequestCode());
    }
}
