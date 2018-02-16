package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import android.support.v4.app.DialogFragment;
import android.view.View;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.WidgetProvider;
import ru.surfstudio.android.core.ui.base.screen.dialog.simple.CoreSimpleDialogInterface;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;

/**
 * DialogNavigator работающий из активити
 */
public class DialogNavigatorForWidget extends DialogNavigator {

    private WidgetProvider widgetProvider;

    public DialogNavigatorForWidget(ActivityProvider activityProvider, WidgetProvider widgetProvider) {
        super(activityProvider);
        this.widgetProvider = widgetProvider;
    }

    @Override
    protected <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(D fragment) {
        fragment.show((View & CoreWidgetViewInterface) widgetProvider.get());
    }
}
