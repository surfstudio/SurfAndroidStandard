package ru.surfstudio.android.navigation.sample_standard.screen.main

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор активити главного экрана
 */
class MainConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : BindableScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule {

        @Provides
        @PerScreen
        fun providePresenters(presenter: MainPresenter) = Any()
    }

    override fun createScreenComponent(defaultActivityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainConfigurator_MainScreenComponent.builder()
                .activityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule())
                .build()
    }
}
