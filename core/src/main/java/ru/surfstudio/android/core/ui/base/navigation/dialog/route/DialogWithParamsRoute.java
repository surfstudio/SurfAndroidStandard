package ru.surfstudio.android.core.ui.base.navigation.dialog.route;

import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.screen.dialog.BaseDialogFragment;

/**
 * см {@link DialogRoute}
 */
public abstract class DialogWithParamsRoute extends DialogRoute {

    @Override
    protected abstract Class<? extends BaseDialogFragment> getFragmentClass();

    protected abstract Bundle prepareBundle();

    @Override
    public BaseDialogFragment createFragment(){
        BaseDialogFragment fragment = super.createFragment();
        fragment.setArguments(prepareBundle());
        return fragment;
    }
}