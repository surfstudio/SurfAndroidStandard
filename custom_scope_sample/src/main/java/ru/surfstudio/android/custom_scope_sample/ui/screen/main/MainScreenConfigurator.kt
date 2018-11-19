package ru.surfstudio.android.custom_scope_sample.ui.screen.main

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.configurator.LoginActivityScreenConfigurator
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.LoginActivityComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.dagger.scope.PerScreen

/**
 * Конфигуратор активити главного экрана
 */
class MainScreenConfigurator(intent: Intent) : LoginActivityScreenConfigurator(intent) {

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentActivityComponent: LoginActivityComponent,
            activityScreenModule: ActivityScreenModule?,
            intent: Intent?
    ): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .loginActivityComponent(parentActivityComponent)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }

    @PerScreen
    @Component(dependencies = [LoginActivityComponent::class],
            modules = [ActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : ScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(route: MainActivityRoute)
        : CustomScreenModule<MainActivityRoute>(route)

}
