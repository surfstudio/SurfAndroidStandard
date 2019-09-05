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
import ru.surfstudio.standard.f_debug.memory.MemoryDebugActivityRoute
import ru.surfstudio.standard.f_debug.memory.MemoryDebugActivityView

/**
 * Конфигуратор экрана Memory
 */
class MemoryDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, MemoryDebugScreenModule::class])
    interface MemoryDebugScreenComponent : ScreenComponent<MemoryDebugActivityView>

    @Module
    internal class MemoryDebugScreenModule(route: MemoryDebugActivityRoute)
        : CustomScreenModule<MemoryDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMemoryDebugScreenConfigurator_MemoryDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .memoryDebugScreenModule(MemoryDebugScreenModule(MemoryDebugActivityRoute()))
                .build()
    }
}