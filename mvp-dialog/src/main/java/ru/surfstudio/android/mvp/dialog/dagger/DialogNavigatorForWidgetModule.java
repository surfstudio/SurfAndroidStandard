package ru.surfstudio.android.mvp.dialog.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.dagger.scope.PerScreen;
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator;
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForWidget;
import ru.surfstudio.android.mvp.widget.provider.WidgetProvider;

/**
 * Поставляет {@link DialogNavigator} для конкретного типа экрана
 */

@Module
public class DialogNavigatorForWidgetModule {

    @Provides
    @PerScreen
    DialogNavigator provideDialogNavigator(ActivityProvider activityProvider, WidgetProvider widgetProvider) {
        return new DialogNavigatorForWidget(activityProvider, widgetProvider);
    }
}
