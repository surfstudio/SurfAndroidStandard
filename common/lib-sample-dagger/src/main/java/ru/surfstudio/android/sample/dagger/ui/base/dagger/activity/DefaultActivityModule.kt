package ru.surfstudio.android.sample.dagger.ui.base.dagger.activity

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.permission.PermissionManagerForActivity
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.state.ActivityScreenState
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.message.DefaultMessageController
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.picturechooser.PicturePermissionChecker
import ru.surfstudio.android.picturechooser.PictureProvider
import ru.surfstudio.android.rxbus.RxBus
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

/**
 * Module for Dagger Activity Component.
 * Provides entities for @PerActivity scope which are not provided in child scopes.
 * These entities could be used without Presenter,
 * for example, opening a fragment using FragmentNavigator from activity container.
 * These entities also could be used in other objects from @PerScreen scope
 */
@Module
class DefaultActivityModule(private val persistentScope: ActivityPersistentScope) {

    @Provides
    @PerActivity
    internal fun provideActivityPersistentScope(): ActivityPersistentScope {
        return persistentScope
    }

    @Provides
    @PerActivity
    internal fun providePersistentScope(persistentScope: ActivityPersistentScope): PersistentScope {
        return persistentScope
    }

    @Provides
    @PerActivity
    internal fun provideActivityScreenState(): ActivityScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerActivity
    internal fun provideActivityProvider(): ActivityProvider {
        return ActivityProvider(persistentScope.screenState)
    }

    @Provides
    @PerActivity
    internal fun provideActivityNavigator(
            activityProvider: ActivityProvider,
            eventDelegateManager: ScreenEventDelegateManager
    ): ActivityNavigator {
        return ActivityNavigatorForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerActivity
    internal fun provideEventDelegateManager(): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerActivity
    internal fun providePermissionManager(
            eventDelegateManager: ScreenEventDelegateManager,
            activityNavigator: ActivityNavigator,
            @Named(NO_BACKUP_SHARED_PREF) sharedPreferences: SharedPreferences,
            activityProvider: ActivityProvider
    ): PermissionManager {
        return PermissionManagerForActivity(
                eventDelegateManager,
                activityNavigator,
                sharedPreferences,
                activityProvider
        )
    }

    @Provides
    @PerActivity
    internal fun provideMessageController(activityProvider: ActivityProvider): MessageController {
        return DefaultMessageController(activityProvider)
    }

    @Provides
    @PerActivity
    internal fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator {
        return FragmentNavigator(activityProvider)
    }

    @Provides
    @PerActivity
    internal fun provideRxBus(): RxBus {
        return RxBus()
    }

    @Provides
    @PerActivity
    internal fun providePicturePermissionChecker(permissionManager: PermissionManager): PicturePermissionChecker {
        return PicturePermissionChecker(permissionManager)
    }

    @Provides
    @PerActivity
    internal fun providePictureProvider(
            context: Context,
            activityNavigator: ActivityNavigator,
            activityProvider: ActivityProvider,
            ppChecker: PicturePermissionChecker
    ): PictureProvider {
        return PictureProvider(context, activityNavigator, activityProvider, ppChecker)
    }
}
