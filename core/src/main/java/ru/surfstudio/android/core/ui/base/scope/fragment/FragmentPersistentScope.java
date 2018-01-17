package ru.surfstudio.android.core.ui.base.scope.fragment;


import android.app.Activity;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.scope.PersistentScope;

public class FragmentPersistentScope extends PersistentScope<FragmentScreenEventDelegateManager> {
    private Activity activity;
    private boolean activityRecreated;
    private boolean activityRestoredFromDisk;

    public FragmentPersistentScope(String name, FragmentScreenEventDelegateManager screenEventDelegateManager) {
        super(name, ScreenType.FRAGMENT, screenEventDelegateManager);
        screenEventDelegateManager.registerDelegate(new FragmentPersistentScopeChangeObserver(this));
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isActivityRecreated() {
        return activityRecreated;
    }

    public void setActivityRecreated(boolean activityRecreated) {
        this.activityRecreated = activityRecreated;
    }

    public boolean isActivityRestoredFromDisk() {
        return activityRestoredFromDisk;
    }

    public void setActivityRestoredFromDisk(boolean activityRestoredFromDisk) {
        this.activityRestoredFromDisk = activityRestoredFromDisk;
    }
}
