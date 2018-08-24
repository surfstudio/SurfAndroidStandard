package ru.surfstudio.android.firebase.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.AnalyticsModule
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.AnalyticsService
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

@PerApplication
@Component(modules = [
    DefaultAppModule::class,
    AnalyticsModule::class])
interface CustomAppComponent : DefaultAppComponent {
    fun analyticsService(): AnalyticsService
}
