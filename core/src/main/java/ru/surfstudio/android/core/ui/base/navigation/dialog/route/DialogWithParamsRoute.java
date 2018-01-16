package ru.surfstudio.android.core.ui.base.navigation.dialog.route;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import ru.surfstudio.android.core.ui.base.screen.dialog.BaseDialogFragment;

/**
 * см {@link DialogRoute}
 */
public abstract class DialogWithParamsRoute extends DialogRoute {

    @Override
    protected abstract Class<? extends DialogFragment> getFragmentClass();

    protected abstract Bundle prepareBundle();

    @Override
    public DialogFragment createFragment(){
        DialogFragment fragment = super.createFragment();
        fragment.setArguments(prepareBundle());
        return fragment;
    }
}