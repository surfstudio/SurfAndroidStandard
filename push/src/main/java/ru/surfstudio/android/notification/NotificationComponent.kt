package ru.surfstudio.android.notification

import dagger.Component
import ru.surfstudio.android.core.app.scheduler.SchedulerModule
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.notification.ui.notification.PushHandler

@PerApplication
@Component(modules = [NotificationModule::class,
    SchedulerModule::class])
interface NotificationComponent {
    fun pushHandler(): PushHandler
}