package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityRoute
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityView
import ru.surfstudio.standard.f_debug.injector.ui.DebugActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.configurator.DebugActivityScreenConfigurator
import ru.surfstudio.standard.f_debug.injector.ui.screen.DebugActivityScreenModule
import ru.surfstudio.standard.f_debug.injector.ui.screen.DebugCustomScreenModule

/**
 * Конфигуратор экрана показа fcm-токена
 */
class FcmDebugScreenConfigurator(intent: Intent) : DebugActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DebugActivityComponent::class],
            modules = [DebugActivityScreenModule::class, FcmDebugScreenModule::class]
    )
    interface FcmDebugScreenComponent : ScreenComponent<FcmDebugActivityView>

    @Module
    internal class FcmDebugScreenModule(route: FcmDebugActivityRoute)
        : DebugCustomScreenModule<FcmDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: DebugActivityComponent,
                                       activityScreenModule: DebugActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerFcmDebugScreenConfigurator_FcmDebugScreenComponent.builder()
                .debugActivityComponent(parentComponent)
                .debugActivityScreenModule(activityScreenModule)
                .fcmDebugScreenModule(FcmDebugScreenModule(FcmDebugActivityRoute()))
                .build()
    }
}
