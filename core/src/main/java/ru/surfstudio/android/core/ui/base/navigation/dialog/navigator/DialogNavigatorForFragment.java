package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.screen.dialog.simple.CoreSimpleDialogInterface;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;

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
        dialog.show((Fragment & CoreFragmentViewInterface) fragmentProvider.get());
    }
}
