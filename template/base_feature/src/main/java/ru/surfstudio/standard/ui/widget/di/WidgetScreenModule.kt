package ru.surfstudio.standard.ui.widget.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.ScreenType
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.core.ui.state.FragmentScreenState
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.DefaultMessageController
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForWidget
import ru.surfstudio.android.mvp.widget.provider.WidgetProvider
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState
import ru.surfstudio.standard.ui.error.ErrorHandlerModule
import ru.surfstudio.standard.ui.screen_modules.ScreenModule
import javax.inject.Named

private const val PARENT_TYPE_DAGGER_NAME = "parent_type"

@Module(includes = [ErrorHandlerModule::class])
class WidgetScreenModule(private val persistentScope: WidgetViewPersistentScope) : ScreenModule() {

    @Provides
    @PerScreen
    internal fun provideDialogNavigator(
            activityProvider: ActivityProvider,
            widgetProvider: WidgetProvider
    ): DialogNavigator {
        return DialogNavigatorForWidget(activityProvider, widgetProvider, persistentScope)
    }

    @Provides
    @PerScreen
    internal fun providePersistentScope(): ScreenPersistentScope {
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
    internal fun provideScreenState(persistentScope: ScreenPersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideWidgetScreenState(persistentScope: WidgetViewPersistentScope): WidgetScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(
            activityProvider: ActivityProvider,
            eventDelegateManager: ScreenEventDelegateManager,
            @Named(PARENT_TYPE_DAGGER_NAME) parentType: ScreenType
    ): ActivityNavigator {
        return if (parentType == ScreenType.FRAGMENT)
            ActivityNavigatorForFragment(activityProvider, createFragmentProvider(), eventDelegateManager)
        else
            ActivityNavigatorForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManager(): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(
            activityProvider: ActivityProvider,
            @Named(PARENT_TYPE_DAGGER_NAME) screenType: ScreenType
    ): MessageController {
        return DefaultMessageController(
                activityProvider,
                if (screenType == ScreenType.FRAGMENT)
                    createFragmentProvider()
                else
                    null
        )
    }

    private fun createFragmentProvider(): FragmentProvider {
        if (persistentScope.screenState.parentType != ScreenType.FRAGMENT) {
            throw IllegalStateException("FragmentProvider can be created only if parent id Fragment")
        }
        return FragmentProvider(persistentScope.screenState.parentState as FragmentScreenState)
    }
}