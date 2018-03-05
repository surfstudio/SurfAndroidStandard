package ru.surfstudio.standard.ui.base.dagger.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.message.DefaultMessageController
import ru.surfstudio.android.core.ui.message.MessageController
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.permission.PermissionManagerForActivity
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForActivity
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = [ErrorHandlerModule::class])
class ActivityScreenModule : ScreenModule() {
    @Provides
    @PerScreen
    internal fun providePersistentScope(persistentScope: ActivityPersistentScope): PersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    internal fun provideScreenState(persistentScope: ActivityPersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManagerProvider(persistentScope: PersistentScope): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerScreen
    internal fun providePermissionManager(activityProvider: ActivityProvider,
                                          eventDelegateManager: ScreenEventDelegateManager): PermissionManager {
        return PermissionManagerForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(activityProvider: ActivityProvider): MessageController {
        return DefaultMessageController(activityProvider)
    }

    @Provides
    @PerScreen
    internal fun provideDialogNavigator(activityProvider: ActivityProvider): DialogNavigator {
        return DialogNavigatorForActivity(activityProvider)
    }
}