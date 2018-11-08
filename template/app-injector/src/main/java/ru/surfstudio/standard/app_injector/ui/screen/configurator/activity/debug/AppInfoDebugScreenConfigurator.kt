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
import ru.surfstudio.standard.f_debug.info.AppInfoDebugActivityRoute
import ru.surfstudio.standard.f_debug.info.AppInfoDebugActivityView

/**
 * Конфигуратор экрана показа общей информации
 */
class AppInfoDebugScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, AppInfoDebugScreenModule::class])
    interface AppInfoDebugScreenComponent
        : ScreenComponent<AppInfoDebugActivityView>

    @Module
    internal class AppInfoDebugScreenModule(route: AppInfoDebugActivityRoute)
        : CustomScreenModule<AppInfoDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerAppInfoDebugScreenConfigurator_AppInfoDebugScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .appInfoDebugScreenModule(AppInfoDebugScreenModule(AppInfoDebugActivityRoute()))
                .build()
    }
}
