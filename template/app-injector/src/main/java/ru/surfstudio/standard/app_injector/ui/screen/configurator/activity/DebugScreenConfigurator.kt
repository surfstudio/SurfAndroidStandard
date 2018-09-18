package ru.surfstudio.standard.app_injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.app_injector.ui.ActivityComponent
import ru.surfstudio.standard.app_injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.app_injector.ui.screen.CustomScreenModule
import ru.surfstudio.standard.f_debug.DebugActivityRoute
import ru.surfstudio.standard.f_debug.DebugActivityView

/**
 * Конфигуратор экрана показа информации для дебага
 */
class DebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, DebugScreenModule::class])
    interface DebugScreenComponent
        : ScreenComponent<DebugActivityView>

    @Module
    internal class DebugScreenModule(route: DebugActivityRoute)
        : CustomScreenModule<DebugActivityRoute>(route)

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDebugScreenConfigurator_DebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .debugScreenModule(DebugScreenModule(DebugActivityRoute()))
                .build()
    }
}
