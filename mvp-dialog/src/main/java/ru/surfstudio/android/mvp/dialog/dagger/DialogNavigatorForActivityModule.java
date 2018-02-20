package ru.surfstudio.android.mvp.dialog.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.dagger.scope.PerScreen;
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator;
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForActivity;

/**
 * Поставляет {@link DialogNavigator} для конкретного типа экрана
 */

@Module
public class DialogNavigatorForActivityModule {

    @Provides
    @PerScreen
    DialogNavigator provideDialogNavigator(ActivityProvider activityProvider) {
        return new DialogNavigatorForActivity(activityProvider);
    }
}
