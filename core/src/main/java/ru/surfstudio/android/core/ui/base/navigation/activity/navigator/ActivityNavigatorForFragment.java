package ru.surfstudio.android.core.ui.base.navigation.activity.navigator;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;

/**
 * ActivityNavigator раборающий из фрагмента
 */
public class ActivityNavigatorForFragment extends ActivityNavigator {

    private FragmentProvider fragmentProvider;

    public ActivityNavigatorForFragment(ActivityProvider activityProvider,
                                        FragmentProvider fragmentProvider,
                                        ScreenEventDelegateManager eventDelegateManager) {
        super(activityProvider, eventDelegateManager);
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    protected void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle bundle) {
        fragmentProvider.get().startActivityForResult(intent, requestCode, bundle);
    }
}
