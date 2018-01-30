package ru.surfstudio.android.core.ui.base.dagger;


import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigatorForActivity;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigator;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigatorForActivityScreen;
import ru.surfstudio.android.core.ui.base.navigation.fragment.FragmentNavigator;
import ru.surfstudio.android.core.ui.base.permission.PermissionManager;
import ru.surfstudio.android.core.ui.base.permission.PermissionManagerForActivity;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;
import ru.surfstudio.android.dagger.scope.PerScreen;

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
    DialogNavigator provideDialogNavigator(ActivityProvider activityProvider){
        return new DialogNavigatorForActivityScreen(activityProvider);
    }

    @Provides
    @PerScreen
    ActivityNavigator provideActivityNavigator(ActivityProvider activityProvider,
                                               ScreenEventDelegateManager eventDelegateManager) {
        return new ActivityNavigatorForActivity(activityProvider, eventDelegateManager);
    }

    @Provides
    @PerScreen
    FragmentNavigator provideFragmentNavigator(ActivityProvider activityProvider){
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
    MessageController provideMessageController(ActivityProvider activityProvider){
        return new DefaultMessageController(activityProvider);
    }

}
