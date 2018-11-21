package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity

import android.content.Context
import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.injector.ui.ActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.f_debug.injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.f_debug.injector.ui.screen.CustomScreenModule
import ru.surfstudio.standard.f_debug.ui_tools.OverlayPermissionChecker
import ru.surfstudio.standard.f_debug.ui_tools.UiToolsDebugActivityRoute
import ru.surfstudio.standard.f_debug.ui_tools.UiToolsDebugActivityView

/**
 * Конфигуратор экрана показа общей информации
 */
class UiToolsDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, UiToolsDebugScreenModule::class])
    interface UiToolsDebugScreenComponent
        : ScreenComponent<UiToolsDebugActivityView>

    @Module
    internal class UiToolsDebugScreenModule(route: UiToolsDebugActivityRoute)
        : CustomScreenModule<UiToolsDebugActivityRoute>(route) {

        @Provides
        fun provideOverlayPermissionChecker(
                context: Context,
                activityNavigator: ActivityNavigator
        ): OverlayPermissionChecker {
            return OverlayPermissionChecker(context, activityNavigator)
        }
    }

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerUiToolsDebugScreenConfigurator_UiToolsDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .uiToolsDebugScreenModule(UiToolsDebugScreenModule(UiToolsDebugActivityRoute()))
                .build()
    }
}
