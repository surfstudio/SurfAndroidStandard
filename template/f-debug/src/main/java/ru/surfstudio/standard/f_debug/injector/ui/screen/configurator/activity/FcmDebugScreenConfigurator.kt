package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.injector.ui.ActivityComponent
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityRoute
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityView
import ru.surfstudio.standard.f_debug.injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.f_debug.injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.f_debug.injector.ui.screen.CustomScreenModule

/**
 * Конфигуратор экрана показа fcm-токена
 */
class FcmDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, FcmDebugScreenModule::class])
    interface FcmDebugScreenComponent
        : ScreenComponent<FcmDebugActivityView>

    @Module
    internal class FcmDebugScreenModule(route: FcmDebugActivityRoute)
        : CustomScreenModule<FcmDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerFcmDebugScreenConfigurator_FcmDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .fcmDebugScreenModule(FcmDebugScreenModule(FcmDebugActivityRoute()))
                .build()
    }
}
