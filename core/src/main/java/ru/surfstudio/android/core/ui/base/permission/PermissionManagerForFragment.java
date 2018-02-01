package ru.surfstudio.android.core.ui.base.permission;


import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;

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
