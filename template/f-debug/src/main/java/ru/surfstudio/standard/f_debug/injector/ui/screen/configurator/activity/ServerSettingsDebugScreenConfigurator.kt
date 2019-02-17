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
import ru.surfstudio.standard.f_debug.server_settings.ServerSettingsDebugActivityRoute
import ru.surfstudio.standard.f_debug.server_settings.ServerSettingsDebugActivityView

/**
 * Конфигуратор экрана настроек сервера
 */
class ServerSettingsDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, ServerSettingsDebugScreenModule::class])
    interface ServerSettingsDebugScreenComponent : ScreenComponent<ServerSettingsDebugActivityView>

    @Module
    internal class ServerSettingsDebugScreenModule(route: ServerSettingsDebugActivityRoute)
        : CustomScreenModule<ServerSettingsDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerServerSettingsDebugScreenConfigurator_ServerSettingsDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .serverSettingsDebugScreenModule(ServerSettingsDebugScreenModule(ServerSettingsDebugActivityRoute()))
                .build()
    }
}