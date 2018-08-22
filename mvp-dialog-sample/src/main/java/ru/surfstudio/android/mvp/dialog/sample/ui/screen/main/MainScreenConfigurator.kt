package ru.surfstudio.android.mvp.dialog.sample.ui.screen.main

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.SimpleDialogComponent
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.SimpleDialogPresenter
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom.SimpleBottomSheetDialogComponent
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom.SimpleBottomSheetDialogPresenter
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
        : ScreenComponent<MainActivityView>, SimpleDialogComponent, SimpleBottomSheetDialogComponent

    @Module
    internal class MainScreenModule(route: MainActivityRoute)
        : CustomScreenModule<MainActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideSimpleDialogPresenter(presenter: MainPresenter): SimpleDialogPresenter {
            return presenter
        }

        @Provides
        @PerScreen
        fun provideSimpleBottomSheetDialogPresenter(presenter: MainPresenter): SimpleBottomSheetDialogPresenter {
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
