package ru.surfstudio.standard.ui.base.dagger

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.message.DefaultMessageController
import ru.surfstudio.android.core.ui.message.MessageController
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.permission.PermissionManagerForFragment
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.dagger.DialogNavigatorForFragmentModule
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = [
    ErrorHandlerModule::class,
    DialogNavigatorForFragmentModule::class
])
class FragmentScreenModule(private val persistentScope: FragmentPersistentScope) {

    @Provides
    @PerScreen
    internal fun providePersistentScope(): PersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    internal fun provideScreenState(persistentScope: PersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideFragmentProvider(): FragmentProvider {
        return FragmentProvider(persistentScope.screenState)
    }

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(activityProvider: ActivityProvider,
                                          fragmentProvider: FragmentProvider,
                                          eventDelegateManager: ScreenEventDelegateManager): ActivityNavigator {
        return ActivityNavigatorForFragment(activityProvider, fragmentProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator {
        return FragmentNavigator(activityProvider)
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManager(): ScreenEventDelegateManager {
        return persistentScope.getScreenEventDelegateManager()
    }

    @Provides
    @PerScreen
    internal fun providePermissionManager(activityProvider: ActivityProvider,
                                          fragmentProvider: FragmentProvider,
                                          eventDelegateManager: ScreenEventDelegateManager): PermissionManager {
        return PermissionManagerForFragment(activityProvider, fragmentProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(activityProvider: ActivityProvider, fragmentProvider: FragmentProvider): MessageController {
        return DefaultMessageController(activityProvider, fragmentProvider)
    }

    @PerScreen
    @Provides
    internal fun provideBaseDependency(schedulersProvider: SchedulersProvider,
                                       screenState: ScreenState,
                                       eventDelegateManager: ScreenEventDelegateManager,
                                       connectionProvider: ConnectionProvider,
                                       activityNavigator: ActivityNavigator): BasePresenterDependency {
        return BasePresenterDependency(schedulersProvider,
                screenState,
                eventDelegateManager,
                connectionProvider,
                activityNavigator)
    }
}