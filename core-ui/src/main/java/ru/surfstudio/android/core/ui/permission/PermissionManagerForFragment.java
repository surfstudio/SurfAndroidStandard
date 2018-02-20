package ru.surfstudio.android.core.ui.permission;


import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;

/**
 * PermissionManager, работающий из фрагмента
 */
public class PermissionManagerForFragment extends PermissionManager {

    private final FragmentProvider fragmentProvider;

    public PermissionManagerForFragment(ActivityProvider activityProvider,
                                        FragmentProvider fragmentProvider,
                                        ScreenEventDelegateManager eventDelegateManager) {
        super(activityProvider, eventDelegateManager);
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    protected void requestPermission(PermissionRequest request) {
        fragmentProvider.get().requestPermissions(request.getPermissions(), request.getRequestCode());
    }
}
