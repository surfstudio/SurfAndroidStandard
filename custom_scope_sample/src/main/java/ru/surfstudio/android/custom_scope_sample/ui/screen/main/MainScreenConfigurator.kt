package ru.surfstudio.android.custom_scope_sample.ui.screen.main

import android.content.Context
import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.dagger.scope.PerScreen

/**
 * Конфигуратор активити главного экрана
 */
class MainScreenConfigurator(context: Context, intent: Intent) : ActivityScreenConfigurator(intent) {
    override fun createScreenComponent(parentActivityComponent: ActivityComponent?, activityScreenModule: ActivityScreenModule?, intent: Intent?): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(parentActivityComponent)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : ScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(route: MainActivityRoute)
        : CustomScreenModule<MainActivityRoute>(route)

}
