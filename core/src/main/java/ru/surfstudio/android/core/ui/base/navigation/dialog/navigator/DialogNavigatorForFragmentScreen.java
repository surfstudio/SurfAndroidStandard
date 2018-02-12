package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.screen.dialog.simple.CoreSimpleDialogFragment;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentView;

/**
 * DialogNavigator работающий из фрагмента
 */
public class DialogNavigatorForFragmentScreen extends DialogNavigator {

    private FragmentProvider fragmentProvider;

    public DialogNavigatorForFragmentScreen(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        super(activityProvider);
        this.fragmentProvider = fragmentProvider;
    }

    @Override
    protected void show(CoreSimpleDialogFragment fragment) {
        fragment.show((CoreFragmentView) fragmentProvider.get());
    }
}
