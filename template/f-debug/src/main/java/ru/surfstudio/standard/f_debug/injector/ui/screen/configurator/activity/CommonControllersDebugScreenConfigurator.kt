package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.reused_components.ReusedComponentsDebugActivityRoute
import ru.surfstudio.standard.f_debug.reused_components.ReusedComponentsDebugActivityView
import ru.surfstudio.standard.f_debug.injector.ui.ActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.f_debug.injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.f_debug.injector.ui.screen.CustomScreenModule

/**
 * Конфигуратор экрана для показа переиспользуемых компонентов
 */
class ReusedComponentsDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, ReusedComponentsDebugScreenModule::class])
    interface ReusedComponentsDebugScreenComponent
        : ScreenComponent<ReusedComponentsDebugActivityView>

    @Module
    internal class ReusedComponentsDebugScreenModule(route: ReusedComponentsDebugActivityRoute)
        : CustomScreenModule<ReusedComponentsDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerReusedComponentsDebugScreenConfigurator_ReusedComponentsDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .reusedComponentsDebugScreenModule(ReusedComponentsDebugScreenModule(ReusedComponentsDebugActivityRoute()))
                .build()
    }
}
