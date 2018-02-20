package ru.surfstudio.android.core.ui.dagger;

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
import ru.surfstudio.android.core.ui.state.ActivityScreenState;
import ru.surfstudio.android.dagger.scope.PerActivity;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Модуль для dagger Activity Component
 * поставляет ряд сущностей, например навигаторы, причем они находятся в @PerActivity scope
 * и не пробрасываются в дочерние scope, эти обьекты могут быть использованы без презентера,
 * например открытие необходимого фрагмента с помощью FragmentNavigator из активити контейнера.
 * Эти обьекты могут также использоваться внутри дополнительных обектов со специфической логикой,
 * принадлежащих скоупу @PerScreen
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

    @Provides
    @PerScreen
    FragmentNavigator provideFragmentNavigator(ActivityProvider activityProvider) {
        return new FragmentNavigator(activityProvider);
    }

}
