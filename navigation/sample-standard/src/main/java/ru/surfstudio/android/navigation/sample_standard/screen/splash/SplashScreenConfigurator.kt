package ru.surfstudio.android.navigation.sample_standard.screen.splash

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
class SplashScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, SplashScreenModule::class])
    internal interface SplashScreenComponent
        : BindableScreenComponent<SplashActivityView>

    @Module
    internal class SplashScreenModule: ScreenModule() {

        @Provides
        @PerScreen
        fun providePresenters(presenter: SplashPresenter) = Any()
    }

    override fun createScreenComponent(defaultActivityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerSplashScreenConfigurator_SplashScreenComponent.builder()
                .activityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .splashScreenModule(SplashScreenModule())
                .build()
    }
}
