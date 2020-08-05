package ru.surfstudio.android.navigation.sample_standard.screen.main

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.navigation.sample_standard.di.ui.screen.ScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор активити главного экрана
 */
class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : BindableScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule : ScreenModule() {

        @Provides
        @PerScreen
        fun providePresenters(presenter: MainPresenter) = Any()
    }

    override fun createScreenComponent(defaultActivityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule())
                .build()
    }
}
