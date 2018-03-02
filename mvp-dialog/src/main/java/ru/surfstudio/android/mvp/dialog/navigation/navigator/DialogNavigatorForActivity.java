package ru.surfstudio.android.mvp.dialog.navigation.navigator;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;

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
        fragment.show(((FragmentActivity & CoreActivityViewInterface) activityProvider.get()).getPersistentScope());
    }
}
