package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityView;
import ru.surfstudio.android.core.ui.base.screen.dialog.BaseSimpleDialogFragment;

public class DialogNavigatorForActivityScreen extends DialogNavigator {

    private ActivityProvider activityProvider;

    public DialogNavigatorForActivityScreen(ActivityProvider activityProvider) {
        super(activityProvider);
        this.activityProvider = activityProvider;
    }

    @Override
    protected void show(BaseSimpleDialogFragment fragment) {
        fragment.show((CoreActivityView) activityProvider.get());
    }
}
