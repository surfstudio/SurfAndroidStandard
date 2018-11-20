package ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.debug

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.app_injector.ui.ActivityComponent
import ru.surfstudio.standard.app_injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.app_injector.ui.screen.CustomScreenModule
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
        : CustomScreenModule<UiToolsDebugActivityRoute>(route)

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
