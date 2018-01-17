package ru.surfstudio.android.core.ui.base.scope.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.activity.OnCreateActivityDelegate;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy.OnDestroyDelegate;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnRestoreStateDelegate;
import ru.surfstudio.android.core.ui.base.scope.BasePersistentScopeChangeObserver;

class ActivityPersistentScopeChangeObserver extends BasePersistentScopeChangeObserver implements
        OnCreateActivityDelegate,
        OnDestroyDelegate,
        OnRestoreStateDelegate {

    private ActivityPersistentScope activityScope;
    private boolean activityClearedAtLeastOnce = false;

    public ActivityPersistentScopeChangeObserver(ActivityPersistentScope activityScope) {
        super(activityScope);
        this.activityScope = activityScope;
    }

    @Override
    public void onDestroy() {
        activityScope.setActivity(null);
        activityClearedAtLeastOnce = true;
    }

    @Override
    public void onCreate(Activity activity) {
        activityScope.setActivity(activity);
    }

    @Override
    public void onRestoreState(@Nullable Bundle savedInstanceState) {
        activityScope.setActivityRecreated(activityClearedAtLeastOnce);
        activityScope.setActivityRestoredFromDisk(!activityClearedAtLeastOnce && savedInstanceState != null );
    }
}
