package ru.surfstudio.android.core.mvp.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.message.MessageController;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment;
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator;
import ru.surfstudio.android.core.ui.permission.PermissionManager;
import ru.surfstudio.android.core.ui.permission.PermissionManagerForFragment;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Модуль для dagger Fragment Screen Component
 */
@Module
public class CoreFragmentScreenModule {

    private FragmentPersistentScope persistentScope;

    public CoreFragmentScreenModule(FragmentPersistentScope persistentScope) {
        this.persistentScope = persistentScope;
    }

    @Provides
    @PerScreen
    PersistentScope providePersistentScope() {
        return persistentScope;
    }

    @Provides
    @PerScreen
    ScreenState provideScreenState(PersistentScope persistentScope) {
        return persistentScope.getScreenState();
    }

    @Provides
    @PerScreen
    FragmentProvider provideFragmentProvider() {
        return new FragmentProvider(persistentScope.getScreenState());
    }

    @Provides
    @PerScreen
    ActivityNavigator provideActivityNavigator(ActivityProvider activityProvider,
                                               FragmentProvider fragmentProvider,
                                               ScreenEventDelegateManager eventDelegateManager) {
        return new ActivityNavigatorForFragment(activityProvider, fragmentProvider, eventDelegateManager);
    }

    @Provides
    @PerScreen
    FragmentNavigator provideFragmentNavigator(ActivityProvider activityProvider) {
        return new FragmentNavigator(activityProvider);
    }

    @Provides
    @PerScreen
    ScreenEventDelegateManager provideEventDelegateManager() {
        return persistentScope.getScreenEventDelegateManager();
    }

    @Provides
    @PerScreen
    PermissionManager providePermissionManager(ActivityProvider activityProvider,
                                               FragmentProvider fragmentProvider,
                                               ScreenEventDelegateManager eventDelegateManager) {
        return new PermissionManagerForFragment(activityProvider, fragmentProvider, eventDelegateManager);
    }

    @Provides
    @PerScreen
    MessageController provideMessageController(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        return new DefaultMessageController(activityProvider, fragmentProvider);
    }
}
