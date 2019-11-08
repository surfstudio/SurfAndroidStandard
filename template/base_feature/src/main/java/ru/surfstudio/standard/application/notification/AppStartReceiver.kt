package ru.surfstudio.standard.application.notification

import ru.surfstudio.android.notification.service.BaseAppStartReceiver
import ru.surfstudio.android.notification.service.BaseNotificationProcessStarter
import javax.inject.Inject

class AppStartReceiver : BaseAppStartReceiver<NotificationProcessStarter>() {

    @Inject
    lateinit var starter: NotificationProcessStarter

    override fun getNotificationProcessStarter(): NotificationProcessStarter = starter
}