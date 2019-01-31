package ru.surfstudio.android.core.mvp.rx.sample

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

/**
 * Конфигуратор активити главного экрана
 */
class MainScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : ScreenComponent<MainActivityView>, SampleDialogComponent

    @Module
    internal class MainScreenModule(route: MainActivityRoute)
        : DefaultCustomScreenModule<MainActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideStandardDialogPresenter(vb: MainViewBinding): SampleDialogViewBinding {
            return vb
        }

        @Provides
        @PerScreen
        fun provideCounterVB(vb: MainViewBinding): CounterViewBinding {
            return vb
        }

        @Provides
        @PerScreen
        fun provideMainNavigationVB(vb: MainViewBinding): MainNavigationViewBinding {
            return vb
        }

        @Provides
        @PerScreen
        fun dialogControlVB(vb: MainViewBinding): DialogControlViewBinding {
            return vb
        }

        @Provides
        @PerScreen
        fun doubleTextViewBindingVB(vb: MainViewBinding): DoubleTextViewBinding {
            return vb
        }
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
