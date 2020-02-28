package ru.surfstudio.android.sample.dagger.ui.base.dagger.screen

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.permission.PermissionManagerForFragment
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.core.ui.state.FragmentScreenState
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.error.DefaultErrorHandlerModule
import ru.surfstudio.android.message.DefaultMessageController
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForFragment
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

@Module(includes = [DefaultErrorHandlerModule::class])
class DefaultFragmentScreenModule(private val persistentScope: FragmentViewPersistentScope) : DefaultScreenModule() {

    @Provides
    @PerScreen
    internal fun providePersistentScope(): ScreenPersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    internal fun provideScreenState(persistentScope: ScreenPersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManager(): ScreenEventDelegateManager {
        return persistentScope.getScreenEventDelegateManager()
    }

    @Provides
    @PerScreen
    internal fun providePermissionManager(eventDelegateManager: ScreenEventDelegateManager,
                                          activityProvider: ActivityProvider,
                                          activityNavigator: ActivityNavigator,
                                          @Named(NO_BACKUP_SHARED_PREF) sharedPreferences: SharedPreferences,
                                          fragmentProvider: FragmentProvider): PermissionManager {
        return PermissionManagerForFragment(
                eventDelegateManager,
                activityProvider,
                activityNavigator,
                sharedPreferences,
                fragmentProvider
        )
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(activityProvider: ActivityProvider,
                                          fragmentProvider: FragmentProvider): MessageController {
        return DefaultMessageController(activityProvider, fragmentProvider)
    }

    @Provides
    @PerScreen
    internal fun provideFragmentProvider(screenState: ScreenState): FragmentProvider {
        return FragmentProvider(screenState as FragmentScreenState)
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
    internal fun provideDialogNavigator(activityProvider: ActivityProvider,
                                        fragmentProvider: FragmentProvider): DialogNavigator {
        return DialogNavigatorForFragment(activityProvider, fragmentProvider, persistentScope)
    }
}