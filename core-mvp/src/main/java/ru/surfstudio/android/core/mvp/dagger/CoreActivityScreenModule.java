package ru.surfstudio.android.core.mvp.dagger;


import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.message.MessageController;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity;
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator;
import ru.surfstudio.android.core.ui.permission.PermissionManager;
import ru.surfstudio.android.core.ui.permission.PermissionManagerForActivity;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Модуль для dagger Activity Screen Component
 */
@Module
public class CoreActivityScreenModule {

    @Provides
    @PerScreen
    PersistentScope providePersistentScope(ActivityPersistentScope persistentScope) {
        return persistentScope;
    }

    @Provides
    @PerScreen
    ScreenState provideScreenState(ActivityPersistentScope persistentScope) {
        return persistentScope.getScreenState();
    }

    @Provides
    @PerScreen
    ActivityNavigator provideActivityNavigator(ActivityProvider activityProvider,
                                               ScreenEventDelegateManager eventDelegateManager) {
        return new ActivityNavigatorForActivity(activityProvider, eventDelegateManager);
    }

    @Provides
    @PerScreen
    FragmentNavigator provideFragmentNavigator(ActivityProvider activityProvider) {
        return new FragmentNavigator(activityProvider);
    }

    @Provides
    @PerScreen
    ScreenEventDelegateManager provideEventDelegateManagerProvider(PersistentScope persistentScope) {
        return persistentScope.getScreenEventDelegateManager();
    }

    @Provides
    @PerScreen
    PermissionManager providePermissionManager(ActivityProvider activityProvider,
                                               ScreenEventDelegateManager eventDelegateManager) {
        return new PermissionManagerForActivity(activityProvider, eventDelegateManager);
    }

    @Provides
    @PerScreen
    MessageController provideMessageController(ActivityProvider activityProvider) {
        return new DefaultMessageController(activityProvider);
    }

}
