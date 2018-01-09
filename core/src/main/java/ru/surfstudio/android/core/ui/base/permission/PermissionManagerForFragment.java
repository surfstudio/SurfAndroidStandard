package ru.surfstudio.android.core.ui.base.permission;


import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;

public class PermissionManagerForFragment extends PermissionManager {

    private final FragmentProvider fragmentProvider;

    public PermissionManagerForFragment(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        super(activityProvider);
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    protected void requestPermission(PermissionRequest request) {
        fragmentProvider.get().requestPermissions(request.getPermissions(), request.getRequestCode());
    }
}
