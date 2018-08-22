package ru.surfstudio.android.standarddialog.sample.ui.screen.main

import android.content.Intent
import com.example.standarddialog.StandardDialogComponent
import com.example.standarddialog.StandardDialogPresenter
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.CustomScreenModule

/**
 * Конфигуратор активити главного экрана
 */
internal class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : ScreenComponent<MainActivityView>, StandardDialogComponent

    @Module
    internal class MainScreenModule(route: MainActivityRoute)
        : CustomScreenModule<MainActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideStandardDialogPresenter(presenter: MainPresenter): StandardDialogPresenter {
            return presenter
        }
    }

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
