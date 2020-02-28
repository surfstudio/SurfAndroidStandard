package ru.surfstudio.android.shared.pref.sample.ui.screen.main

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule
import ru.surfstudio.android.shared.pref.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.shared.pref.sample.ui.base.dagger.activity.CustomActivityComponent

/**
 * Конфигуратор активити главного экрана
 */
internal class MainScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent : ScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(route: MainActivityRoute) : DefaultCustomScreenModule<MainActivityRoute>(route)

    override fun createScreenComponent(customActivityComponent: CustomActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        @Suppress("DEPRECATION")
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
