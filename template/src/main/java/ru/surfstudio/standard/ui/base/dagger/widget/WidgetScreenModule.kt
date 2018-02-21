package ru.surfstudio.standard.ui.base.dagger.widget

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.ScreenType
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.message.DefaultMessageController
import ru.surfstudio.android.core.ui.message.MessageController
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.permission.PermissionManagerForActivity
import ru.surfstudio.android.core.ui.permission.PermissionManagerForFragment
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.state.FragmentScreenState
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForWidget
import ru.surfstudio.android.mvp.widget.provider.WidgetProvider
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule
import javax.inject.Named

private const val PARENT_TYPE_DAGGER_NAME = "parent_type"

@Module(includes = [
    ErrorHandlerModule::class])
class WidgetScreenModule(private val persistentScope: WidgetViewPersistentScope) {
    @Provides
    @PerScreen
    internal fun provideDialogNavigator(activityProvider: ActivityProvider, widgetProvider: WidgetProvider): DialogNavigator {
        return DialogNavigatorForWidget(activityProvider, widgetProvider)
    }

    @Provides
    @PerScreen
    internal fun providePersistentScope(): PersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    internal fun provideWidgetPersistentScope(): WidgetViewPersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    @Named(PARENT_TYPE_DAGGER_NAME)
    internal fun provideParentScreenType(): ScreenType {
        return persistentScope.screenState.parentType
    }

    @Provides
    @PerScreen
    internal fun provideScreenState(persistentScope: PersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideWidgetScreenState(persistentScope: WidgetViewPersistentScope): WidgetScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(activityProvider: ActivityProvider,
                                          eventDelegateManager: ScreenEventDelegateManager,
                                          @Named(PARENT_TYPE_DAGGER_NAME) parentType: ScreenType): ActivityNavigator {
        return if (parentType == ScreenType.FRAGMENT)
            ActivityNavigatorForFragment(activityProvider, createFragmentProvider(), eventDelegateManager)
        else
            ActivityNavigatorForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator {
        return FragmentNavigator(activityProvider)
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManager(): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerScreen
    internal fun providePermissionManager(activityProvider: ActivityProvider,
                                          eventDelegateManager: ScreenEventDelegateManager,
                                          @Named(PARENT_TYPE_DAGGER_NAME) parentType: ScreenType): PermissionManager {
        return if (parentType == ScreenType.FRAGMENT)
            PermissionManagerForFragment(activityProvider, createFragmentProvider(), eventDelegateManager)
        else
            PermissionManagerForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(activityProvider: ActivityProvider, fragmentProvider: FragmentProvider): MessageController {
        return DefaultMessageController(activityProvider, fragmentProvider)
    }

    private fun createFragmentProvider(): FragmentProvider {
        if (persistentScope.screenState.parentType != ScreenType.FRAGMENT) {
            throw IllegalStateException("FragmentProvider can be created only if parent id Fragment")
        }
        return FragmentProvider(persistentScope.screenState.parentState as FragmentScreenState)
    }
}