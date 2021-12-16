package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.custom_scope_sample.ui.base.error.ErrorHandlerModule
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.DefaultMessageController
import ru.surfstudio.android.message.MessageController


@Module(includes = [ErrorHandlerModule::class])
class ActivityScreenModule(private val activityViewPersistentScope: ActivityViewPersistentScope) :
    ScreenModule() {

    @Provides
    @PerScreen
    internal fun providePersistentScope(persistentScope: ActivityPersistentScope): ScreenPersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    internal fun provideScreenState(persistentScope: ActivityPersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManagerProvider(persistentScope: ScreenPersistentScope): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(
        activityProvider: ActivityProvider,
        eventDelegateManager: ScreenEventDelegateManager
    ): ActivityNavigator {
        return ActivityNavigatorForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(activityProvider: ActivityProvider): MessageController {
        return DefaultMessageController(activityProvider)
    }
}