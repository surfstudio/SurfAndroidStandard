package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.common_controllers.CommonControllersDebugActivityRoute
import ru.surfstudio.standard.f_debug.common_controllers.CommonControllersDebugActivityView
import ru.surfstudio.standard.f_debug.injector.ui.ActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.f_debug.injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.f_debug.injector.ui.screen.CustomScreenModule

/**
 * Конфигуратор экрана для показа переиспользуемых контроллеров
 */
class CommonControllersDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, CommonControllersDebugScreenModule::class])
    interface CommonControllersDebugScreenComponent
        : ScreenComponent<CommonControllersDebugActivityView>

    @Module
    internal class CommonControllersDebugScreenModule(route: CommonControllersDebugActivityRoute)
        : CustomScreenModule<CommonControllersDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerCommonControllersDebugScreenConfigurator_CommonControllersDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .commonControllersDebugScreenModule(CommonControllersDebugScreenModule(CommonControllersDebugActivityRoute()))
                .build()
    }
}
