package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.navigation.Navigator;
import ru.surfstudio.android.core.ui.base.navigation.dialog.route.DialogRoute;
import ru.surfstudio.android.core.ui.base.screen.dialog.simple.CoreSimpleDialogInterface;

/**
 * позволяет открывать диалоги
 */
public abstract class DialogNavigator implements Navigator {

    private ActivityProvider activityProvider;

    public DialogNavigator(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    public void show(DialogRoute dialogRoute){
        DialogFragment dialog = dialogRoute.createFragment();
        if(dialog instanceof CoreSimpleDialogInterface) {
            showSimpleDialog((DialogFragment & CoreSimpleDialogInterface) dialog);
        } else {
            dialog.show(activityProvider.get().getSupportFragmentManager(), dialogRoute.getTag());
        }
    }

    public void dismiss(DialogRoute dialogRoute){
        FragmentManager fragmentManager = activityProvider.get().getSupportFragmentManager();
        DialogFragment dialogFragment = (DialogFragment) fragmentManager
                .findFragmentByTag(dialogRoute.getTag());
        dialogFragment.dismiss();
    }

    protected abstract <D extends DialogFragment & CoreSimpleDialogInterface>void showSimpleDialog(D fragment);
    
}
