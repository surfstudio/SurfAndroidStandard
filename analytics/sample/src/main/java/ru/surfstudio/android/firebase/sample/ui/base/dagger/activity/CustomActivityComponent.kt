package ru.surfstudio.android.firebase.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.firebase.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.notification.interactor.push.PushNotificationsListener
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {

    fun analyticsService(): DefaultAnalyticService
    fun pushNotificationsListener(): PushNotificationsListener
}