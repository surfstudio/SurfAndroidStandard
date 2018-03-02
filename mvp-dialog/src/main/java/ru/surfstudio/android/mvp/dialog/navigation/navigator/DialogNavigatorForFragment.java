package ru.surfstudio.android.mvp.dialog.navigation.navigator;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;

/**
 * DialogNavigator работающий из фрагмента
 */
public class DialogNavigatorForFragment extends DialogNavigator {

    private FragmentProvider fragmentProvider;

    public DialogNavigatorForFragment(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        super(activityProvider);
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    protected <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(D dialog) {
        dialog.show(((Fragment & CoreFragmentViewInterface) fragmentProvider.get()).getPersistentScope());
    }
}
