package ru.surfstudio.android.mvp.widget.dagger;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.message.MessageController;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment;
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator;
import ru.surfstudio.android.core.ui.permission.PermissionManager;
import ru.surfstudio.android.core.ui.permission.PermissionManagerForActivity;
import ru.surfstudio.android.core.ui.permission.PermissionManagerForFragment;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.provider.FragmentProvider;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.state.FragmentScreenState;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.dagger.scope.PerScreen;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState;

/**
 * Модуль для dagger Widget Screen Component
 */

@Module
public class CoreWidgetScreenModule {
    private static final String PARENT_TYPE_DAGGER_NAME = "parent_type";

    private WidgetViewPersistentScope persistentScope;

    public CoreWidgetScreenModule(WidgetViewPersistentScope persistentScope) {
        this.persistentScope = persistentScope;
    }

    @Provides
    @PerScreen
    PersistentScope providePersistentScope() {
        return persistentScope;
    }

    @Provides
    @PerScreen
    WidgetViewPersistentScope provideWidgetPersistentScope() {
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
    WidgetScreenState provideWidgetScreenState(WidgetViewPersistentScope persistentScope) {
        return persistentScope.getScreenState();
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
