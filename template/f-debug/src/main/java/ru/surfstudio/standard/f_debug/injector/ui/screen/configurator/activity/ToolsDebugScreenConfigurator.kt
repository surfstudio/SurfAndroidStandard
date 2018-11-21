package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.injector.ui.ActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.f_debug.injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.f_debug.injector.ui.screen.CustomScreenModule
import ru.surfstudio.standard.f_debug.tools.ToolsDebugActivityRoute
import ru.surfstudio.standard.f_debug.tools.ToolsDebugActivityView

/**
 * Конфигуратор экрана показа общей информации
 */
class ToolsDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, ToolsDebugScreenModule::class])
    interface ToolsDebugScreenComponent
        : ScreenComponent<ToolsDebugActivityView>

    @Module
    internal class ToolsDebugScreenModule(route: ToolsDebugActivityRoute)
        : CustomScreenModule<ToolsDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerToolsDebugScreenConfigurator_ToolsDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .toolsDebugScreenModule(ToolsDebugScreenModule(ToolsDebugActivityRoute()))
                .build()
    }
}
