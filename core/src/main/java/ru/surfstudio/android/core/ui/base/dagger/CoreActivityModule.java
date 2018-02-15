package ru.surfstudio.android.core.ui.base.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigatorForActivity;
import ru.surfstudio.android.core.ui.base.navigation.fragment.FragmentNavigator;
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
    FragmentNavigator provideFragmentNavigator(ActivityProvider activityProvider){
        return new FragmentNavigator(activityProvider);
    }

}
