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
import ru.surfstudio.standard.f_debug.info.InfoDebugActivityRoute
import ru.surfstudio.standard.f_debug.info.InfoDebugActivityView

/**
 * Конфигуратор экрана показа общей информации
 */
class InfoDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, InfoDebugScreenModule::class])
    interface InfoDebugScreenComponent
        : ScreenComponent<InfoDebugActivityView>

    @Module
    internal class InfoDebugScreenModule(route: InfoDebugActivityRoute)
        : CustomScreenModule<InfoDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerInfoDebugScreenConfigurator_InfoDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .infoDebugScreenModule(InfoDebugScreenModule(InfoDebugActivityRoute()))
                .build()
    }
}
