package ru.surfstudio.android.mvp.dialog.navigation.route;


import android.support.v4.app.DialogFragment;

import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute;

/**
 * см {@link FragmentRoute}
 */
public abstract class DialogRoute extends FragmentRoute {

    @Override
    protected abstract Class<? extends DialogFragment> getFragmentClass();

    @Override
    public DialogFragment createFragment() {
        return (DialogFragment) super.createFragment();
    }
}
