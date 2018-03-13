package ru.surfstudio.android.pictureprovider.sample.interactor.ui.base.dagger.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.message.DefaultMessageController
import ru.surfstudio.android.core.ui.message.MessageController
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.permission.PermissionManagerForFragment
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.pictureprovider.sample.interactor.ui.base.error.ErrorHandlerModule

@Module(includes = [ErrorHandlerModule::class])
class FragmentScreenModule(private val persistentScope: FragmentPersistentScope) : ScreenModule() {

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
}