package ru.surfstudio.android.core.ui.permission;


import android.support.v4.app.ActivityCompat;

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;

/**
 * PermissionManager, работающий из активити
 */
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
