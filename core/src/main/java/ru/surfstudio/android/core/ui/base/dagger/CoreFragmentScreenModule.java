package ru.surfstudio.android.core.ui.base.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigatorForFragment;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigator;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigatorForFragmentScreen;
import ru.surfstudio.android.core.ui.base.navigation.fragment.ChildFragmentNavigator;
import ru.surfstudio.android.core.ui.base.navigation.fragment.FragmentNavigator;
import ru.surfstudio.android.core.ui.base.permission.PermissionManager;
import ru.surfstudio.android.core.ui.base.permission.PermissionManagerForFragment;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;

@Module
public class CoreFragmentScreenModule {

    private PersistentScope persistentScreenScope;

    public CoreFragmentScreenModule(PersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    @Provides
    @PerScreen
    ActivityProvider provideActivityProvider() {
        return new ActivityProvider(persistentScreenScope);
    }

    @Provides
    @PerScreen
    FragmentProvider provideFragmentProvider() {
        return new FragmentProvider(persistentScreenScope);
    }

    @Provides
    @PerScreen
    DialogNavigator provideDialogNavigator(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        return new DialogNavigatorForFragmentScreen(activityProvider, fragmentProvider);
    }

    @Provides
    @PerScreen
    ActivityNavigator provideActivityNavigator(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        return new ActivityNavigatorForFragment(activityProvider, fragmentProvider);
    }

    @Provides
    @PerScreen
    FragmentNavigator provideFragmentNavigator(ActivityProvider activityProvider) {
        return new FragmentNavigator(activityProvider);
    }

    @Provides
    @PerScreen
    ChildFragmentNavigator provideChildFragmentNavigator(ActivityProvider activityProvider,
                                                         FragmentProvider fragmentProvider) {
        return new ChildFragmentNavigator(activityProvider, fragmentProvider);
    }

    @Provides
    @PerScreen
    ScreenEventDelegateManager provideEventDelegateManager() {
        return persistentScreenScope.getScreenEventDelegateManager();
    }

    @Provides
    @PerScreen
    ActivityScreenEventDelegateManagerProvider provideActivityEventDelegateManagerProvider(ActivityProvider activityProvider) {
        return new ActivityScreenEventDelegateManagerProvider(activityProvider);
    }

    @Provides
    @PerScreen
    PermissionManager providePermissionManager(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        return new PermissionManagerForFragment(activityProvider, fragmentProvider);
    }

    @Provides
    @PerScreen
    MessageController provideMessageController(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        return new DefaultMessageController(activityProvider, fragmentProvider);
    }
}
