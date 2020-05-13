package ru.surfstudio.android.firebase.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.AnalyticsModule
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.android.notification.interactor.push.PushNotificationsListener
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
    fun analyticsService(): DefaultAnalyticService

    fun pushNotificationsListener(): PushNotificationsListener
    fun pushHandler(): PushHandler
}
