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
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootActivityDebugView
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute

/**
 * Конфигуратор экрана перезапуска приложения
 */
class RebootDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, RebootDebugScreenModule::class])
    interface RebootDebugScreenComponent : ScreenComponent<RebootActivityDebugView>

    @Module
    internal class RebootDebugScreenModule(route: RebootDebugActivityRoute)
        : CustomScreenModule<RebootDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerRebootDebugScreenConfigurator_RebootDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .rebootDebugScreenModule(RebootDebugScreenModule(RebootDebugActivityRoute()))
                .build()
    }
}