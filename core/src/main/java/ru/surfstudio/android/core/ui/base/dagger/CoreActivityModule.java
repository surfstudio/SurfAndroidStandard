package ru.surfstudio.android.core.ui.base.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigatorForActivity;
import ru.surfstudio.android.core.ui.base.permission.PermissionManager;
import ru.surfstudio.android.core.ui.base.permission.PermissionManagerForActivity;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;
import ru.surfstudio.android.dagger.scope.PerActivity;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Модуль для dagger Activity Component
 */

@Module
public class CoreActivityModule {

    private ActivityPersistentScope persistentScope;

    public CoreActivityModule(ActivityPersistentScope persistentScope) {
        this.persistentScope = persistentScope;
    }

    @Provides
    @PerActivity
    ActivityPersistentScope provideActivityPersistentScope() {
        return persistentScope;
    }

    @Provides
    @PerScreen
    PersistentScope providePersistentScope(ActivityPersistentScope persistentScope) {
        return persistentScope;
    }

    @Provides
    @PerActivity
    ActivityScreenState provideActivityScreenState() {
        return persistentScope.getScreenState();
    }

    @Provides
    @PerActivity
    ActivityProvider provideActivityProvider() {
        return new ActivityProvider(persistentScope.getScreenState());
    }

    @Provides
    @PerActivity
        //todo описать зачем все это
    ActivityNavigator provideActivityNavigator(ActivityProvider activityProvider,
                                               ScreenEventDelegateManager eventDelegateManager) {
        return new ActivityNavigatorForActivity(activityProvider, eventDelegateManager);
    }

    @Provides
    @PerActivity
    ScreenEventDelegateManager provideEventDelegateManager() {
        return persistentScope.getScreenEventDelegateManager();
    }

    @Provides
    @PerActivity
    PermissionManager providePermissionManager(ActivityProvider activityProvider,
                                               ScreenEventDelegateManager eventDelegateManager) {
        return new PermissionManagerForActivity(activityProvider, eventDelegateManager);
    }

    @Provides
    @PerActivity
    MessageController provideMessageController(ActivityProvider activityProvider) {
        return new DefaultMessageController(activityProvider);
    }

}
