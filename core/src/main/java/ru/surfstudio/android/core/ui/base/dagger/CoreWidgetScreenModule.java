package ru.surfstudio.android.core.ui.base.dagger;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.base.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigatorForActivity;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigatorForFragment;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigator;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigatorForActivityScreen;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigatorForFragmentScreen;
import ru.surfstudio.android.core.ui.base.navigation.fragment.FragmentNavigator;
import ru.surfstudio.android.core.ui.base.permission.PermissionManager;
import ru.surfstudio.android.core.ui.base.permission.PermissionManagerForActivity;
import ru.surfstudio.android.core.ui.base.permission.PermissionManagerForFragment;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.WidgetPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.FragmentScreenState;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Модуль для dagger Widget Screen Component
 */

@Module
public class CoreWidgetScreenModule {
    private static final String PARENT_TYPE_DAGGER_NAME = "parent_type";

    private WidgetPersistentScope persistentScope;

    public CoreWidgetScreenModule(WidgetPersistentScope persistentScope) {
        this.persistentScope = persistentScope;
    }

    @Provides
    @PerScreen
    PersistentScope providePersistentScope() {
        return persistentScope;
    }

    @Provides
    @PerScreen
    @Named(PARENT_TYPE_DAGGER_NAME)
    ScreenType provideParentScreenType() {
        return persistentScope.getScreenState().getParentType();
    }

    @Provides
    @PerScreen
    ScreenState provideScreenState(PersistentScope persistentScope) {
        return persistentScope.getScreenState();
    }

    @Provides
    @PerScreen
    DialogNavigator provideDialogNavigator(ActivityProvider activityProvider,
                                           @Named(PARENT_TYPE_DAGGER_NAME) ScreenType parentType) {
        return parentType == ScreenType.FRAGMENT
                ? new DialogNavigatorForFragmentScreen(activityProvider, createFragmentProvider())
                : new DialogNavigatorForActivityScreen(activityProvider);
    }

    @Provides
    @PerScreen
    ActivityNavigator provideActivityNavigator(ActivityProvider activityProvider,
                                               ScreenEventDelegateManager eventDelegateManager,
                                               @Named(PARENT_TYPE_DAGGER_NAME) ScreenType parentType) {
        return parentType == ScreenType.FRAGMENT
                ? new ActivityNavigatorForFragment(activityProvider, createFragmentProvider(), eventDelegateManager)
                : new ActivityNavigatorForActivity(activityProvider, eventDelegateManager);
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
                                               ScreenEventDelegateManager eventDelegateManager,
                                               @Named(PARENT_TYPE_DAGGER_NAME) ScreenType parentType) {
        return parentType == ScreenType.FRAGMENT
                ? new PermissionManagerForFragment(activityProvider, createFragmentProvider(), eventDelegateManager)
                : new PermissionManagerForActivity(activityProvider, eventDelegateManager);
    }

    @Provides
    @PerScreen
    MessageController provideMessageController(ActivityProvider activityProvider, FragmentProvider fragmentProvider) {
        return new DefaultMessageController(activityProvider, fragmentProvider);
    }

    private FragmentProvider createFragmentProvider() {
        if (persistentScope.getScreenState().getParentType() != ScreenType.FRAGMENT) {
            throw new IllegalStateException("FragmentProvider can be created only if parent id Fragment");
        }
        return new FragmentProvider((FragmentScreenState) persistentScope.getScreenState().getParentState());
    }


}
