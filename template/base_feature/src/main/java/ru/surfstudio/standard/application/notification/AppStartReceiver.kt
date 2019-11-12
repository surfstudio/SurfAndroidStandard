package ru.surfstudio.standard.application.notification

import ru.surfstudio.android.notification.service.BaseAppStartReceiver
import ru.surfstudio.android.notification.service.BaseNotificationProcessStarter
import ru.surfstudio.android.notification.service.NotificationProcessStarterHolder

/**
 * Целевой ресивер, куда приходит ивент о закрытии сервиса [NotificationReviverService],
 * предоставляющий ссылку на [NotificationProcessStarter]
 */
class AppStartReceiver : BaseAppStartReceiver() {
    
    override fun getNotificationProcessStarter(): BaseNotificationProcessStarter =
            NotificationProcessStarterHolder.serviceStarter 
}