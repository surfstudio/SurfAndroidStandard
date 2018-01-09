package ru.surfstudio.android.core.ui.base.navigation.dialog.route;


import ru.surfstudio.android.core.ui.base.navigation.fragment.route.FragmentRoute;
import ru.surfstudio.android.core.ui.base.screen.dialog.BaseDialogFragment;

/**
 * см {@link FragmentRoute}
 */
public abstract class DialogRoute extends FragmentRoute {

    @Override
    protected abstract Class<? extends BaseDialogFragment> getFragmentClass();

    @Override
    public BaseDialogFragment createFragment(){
        return (BaseDialogFragment)super.createFragment();
    }
}
