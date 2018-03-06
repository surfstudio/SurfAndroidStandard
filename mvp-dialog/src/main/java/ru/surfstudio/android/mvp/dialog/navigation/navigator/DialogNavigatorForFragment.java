package ru.surfstudio.android.mvp.dialog.navigation.navigator;


import android.support.v4.app.DialogFragment;

import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;

/**
 * DialogNavigator работающий из фрагмента
 */
public class DialogNavigatorForFragment extends DialogNavigator {

    private FragmentProvider fragmentProvider;
    private FragmentViewPersistentScope fragmentViewPersistentScope;


    public DialogNavigatorForFragment(ActivityProvider activityProvider,
                                      FragmentProvider fragmentProvider,
                                      FragmentViewPersistentScope fragmentViewPersistentScope) {
        super(activityProvider, fragmentViewPersistentScope);
        this.fragmentProvider = fragmentProvider;
        this.fragmentViewPersistentScope = fragmentViewPersistentScope;
    }

    @Override
    protected <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(D dialog) {
        dialog.show(fragmentViewPersistentScope);
    }
}
