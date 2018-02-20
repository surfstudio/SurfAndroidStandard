package ru.surfstudio.android.mvp.dialog.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;
import ru.surfstudio.android.dagger.scope.PerScreen;
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator;
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForFragment;

/**
 * Поставляет {@link DialogNavigator} для конкретного типа экрана
 */

@Module
public class DialogNavigatorForFragmentModule {

    @Provides
    @PerScreen
    DialogNavigator provideDialogNavigator(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        return new DialogNavigatorForFragment(activityProvider, fragmentProvider);
    }
}
