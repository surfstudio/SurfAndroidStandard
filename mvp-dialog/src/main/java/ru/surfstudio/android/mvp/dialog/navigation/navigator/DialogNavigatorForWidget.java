package ru.surfstudio.android.mvp.dialog.navigation.navigator;


import android.support.v4.app.DialogFragment;
import android.view.View;

import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;
import ru.surfstudio.android.mvp.widget.provider.WidgetProvider;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

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
