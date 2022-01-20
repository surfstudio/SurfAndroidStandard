package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.core.ui.state.FragmentScreenState
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.custom_scope_sample.ui.base.error.ErrorHandlerModule
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.DefaultMessageController
import ru.surfstudio.android.message.MessageController

@Module(includes = [ErrorHandlerModule::class])
class FragmentScreenModule(private val persistentScope: FragmentViewPersistentScope) :
    ScreenModule() {

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
    internal fun provideMessageController(
        activityProvider: ActivityProvider,
        fragmentProvider: FragmentProvider
    ): MessageController {
        return DefaultMessageController(activityProvider, fragmentProvider)
    }

    @Provides
    @PerScreen
    internal fun provideFragmentProvider(screenState: ScreenState): FragmentProvider {
        return FragmentProvider(screenState as FragmentScreenState)
    }

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(
        activityProvider: ActivityProvider,
        fragmentProvider: FragmentProvider,
        eventDelegateManager: ScreenEventDelegateManager
    ): ActivityNavigator {
        return ActivityNavigatorForFragment(
            activityProvider,
            fragmentProvider,
            eventDelegateManager
        )
    }
}