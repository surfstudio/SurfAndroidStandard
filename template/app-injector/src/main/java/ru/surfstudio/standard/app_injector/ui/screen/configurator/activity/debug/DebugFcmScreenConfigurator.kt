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
import ru.surfstudio.standard.f_debug.fcm.DebugFcmActivityRoute
import ru.surfstudio.standard.f_debug.fcm.DebugFcmActivityView

/**
 * Конфигуратор экрана показа fcm-токена
 */
class DebugFcmScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, DebugFcmScreenModule::class])
    interface DebugFcmScreenComponent
        : ScreenComponent<DebugFcmActivityView>

    @Module
    internal class DebugFcmScreenModule(route: DebugFcmActivityRoute)
        : CustomScreenModule<DebugFcmActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDebugFcmScreenConfigurator_DebugFcmScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .debugFcmScreenModule(DebugFcmScreenModule(DebugFcmActivityRoute()))
                .build()
    }
}
