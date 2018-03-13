package ru.surfstudio.android.mvp.dialog.navigation.navigator;


import android.support.v4.app.DialogFragment;

import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface;
import ru.surfstudio.android.mvp.widget.provider.WidgetProvider;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;

/**
 * DialogNavigator работающий из активити
 */
public class DialogNavigatorForWidget extends DialogNavigator {

    private WidgetProvider widgetProvider;
    private WidgetViewPersistentScope widgetViewPersistentScope;


    public DialogNavigatorForWidget(ActivityProvider activityProvider,
                                    WidgetProvider widgetProvider,
                                    WidgetViewPersistentScope widgetViewPersistentScope) {
        super(activityProvider, widgetViewPersistentScope);
        this.widgetProvider = widgetProvider;
        this.widgetViewPersistentScope = widgetViewPersistentScope;
    }

    @Override
    protected <D extends DialogFragment & CoreSimpleDialogInterface> void showSimpleDialog(D fragment) {
        fragment.show(widgetViewPersistentScope);
    }
}
