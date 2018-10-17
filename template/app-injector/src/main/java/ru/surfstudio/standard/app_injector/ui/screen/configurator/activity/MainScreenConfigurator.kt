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
import ru.surfstudio.standard.f_main.MainActivityRoute
import ru.surfstudio.standard.f_main.MainActivityView

/**
 * Конфигуратор главного экрана
 */
class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, MainScreenModule::class])
    interface MainScreenComponent
        : ScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(
            route: MainActivityRoute
    ) : CustomScreenModule<MainActivityRoute>(route)

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
