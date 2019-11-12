package ru.surfstudio.standard.application.notification

import android.content.Context
import ru.surfstudio.android.notification.service.BaseNotificationProcessStarter
import ru.surfstudio.android.notification.service.NotificationReviverService
import javax.inject.Inject

/**
 * Целевой класс для запуска [NotificationReviverService],
 * который предоставляет имя класса [AppStartReceiver] для запуска
 */
class NotificationProcessStarter @Inject constructor(
        applicationContext: Context
) : BaseNotificationProcessStarter(applicationContext) {

    override fun getAppStartReceiverClassName(): String 
            = AppStartReceiver::class.java.canonicalName as String
}