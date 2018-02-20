package ru.surfstudio.standard.ui.screen.main

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.dagger.CoreActivityScreenModule
import ru.surfstudio.android.core.mvp.dagger.CustomScreenModule
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule

/**
 * Конфигуратор активити главного экрана
 */
internal class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(ActivityScreenModule::class, MainScreenModule::class))
    internal interface MainScreenComponent : ScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(route: MainActivityRoute) :
            CustomScreenModule<MainActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       coreActivityScreenModule: CoreActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(activityComponent)
                .coreActivityScreenModule(coreActivityScreenModule)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
