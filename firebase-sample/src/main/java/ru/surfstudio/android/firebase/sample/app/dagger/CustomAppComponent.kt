package ru.surfstudio.android.firebase.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.AnalyticsModule
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.AnalyticsService
import ru.surfstudio.android.firebase.sample.ui.common.notification.FirebaseMessagingService
import ru.surfstudio.android.notification.NotificationManager
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule

@PerApplication
@Component(modules = [
    DefaultAppModule::class,
    DefaultSharedPrefModule::class,
    AnalyticsModule::class,
    NotificationModule::class
])
interface CustomAppComponent : DefaultAppComponent {
    fun analyticsService(): AnalyticsService

    fun pushInteractor(): PushInteractor
    fun notificationManager(): NotificationManager

    fun inject(s: FirebaseMessagingService)
}
