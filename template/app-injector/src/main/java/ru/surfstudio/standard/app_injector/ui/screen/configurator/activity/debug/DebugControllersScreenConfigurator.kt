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
import ru.surfstudio.standard.f_debug.debug_controllers.DebugControllersActivityRoute
import ru.surfstudio.standard.f_debug.debug_controllers.DebugControllersActivityView

/**
 * Конфигуратор экрана для показа переиспользуемых контроллеров
 */
class DebugControllersScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, DebugControllersScreenModule::class])
    interface DebugControllersScreenComponent
        : ScreenComponent<DebugControllersActivityView>

    @Module
    internal class DebugControllersScreenModule(route: DebugControllersActivityRoute)
        : CustomScreenModule<DebugControllersActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDebugControllersScreenConfigurator_DebugControllersScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .debugControllersScreenModule(DebugControllersScreenModule(DebugControllersActivityRoute()))
                .build()
    }
}
