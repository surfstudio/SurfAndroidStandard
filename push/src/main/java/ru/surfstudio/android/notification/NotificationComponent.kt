package ru.surfstudio.android.notification

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.notification.ui.notification.PushHandler
import ru.surfstudio.android.rx.extension.scheduler.SchedulerModule

@PerApplication
@Component(modules = [NotificationModule::class,
    SchedulerModule::class])
interface NotificationComponent {
    fun pushHandler(): PushHandler
}