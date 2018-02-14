package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.dialog.simple.CoreSimpleDialogInterface;

/**
 * DialogNavigator работающий из активити
 */
public class DialogNavigatorForActivity extends DialogNavigator {

    private ActivityProvider activityProvider;

    public DialogNavigatorForActivity(ActivityProvider activityProvider) {
        super(activityProvider);
        this.activityProvider = activityProvider;
    }

    @Override
    protected <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(D fragment) {
        fragment.show((FragmentActivity & CoreActivityViewInterface) activityProvider.get());
    }
}
