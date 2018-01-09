package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import android.support.v4.app.FragmentManager;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.navigation.Navigator;
import ru.surfstudio.android.core.ui.base.navigation.dialog.route.DialogRoute;
import ru.surfstudio.android.core.ui.base.screen.dialog.BaseDialogFragment;
import ru.surfstudio.android.core.ui.base.screen.dialog.BaseSimpleDialogFragment;

/**
 * позволяет открывать диалоги
 */
public abstract class DialogNavigator implements Navigator {

    private ActivityProvider activityProvider;

    public DialogNavigator(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    public void show(DialogRoute dialogRoute){
        BaseDialogFragment dialog = dialogRoute.createFragment();
        if(dialog instanceof BaseSimpleDialogFragment) {
            show((BaseSimpleDialogFragment) dialog);
        } else {
            dialog.show(activityProvider.get().getSupportFragmentManager(), dialogRoute.getTag());
        }
    }

    public void dismiss(DialogRoute dialogRoute){
        FragmentManager fragmentManager = activityProvider.get().getSupportFragmentManager();
        BaseDialogFragment dialogFragment = (BaseDialogFragment) fragmentManager
                .findFragmentByTag(dialogRoute.getTag());
        dialogFragment.dismiss();
    }

    protected abstract void show(BaseSimpleDialogFragment fragment);
    
}
