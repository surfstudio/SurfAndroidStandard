package ru.surfstudio.standard.application.notification

import android.content.Context
import ru.surfstudio.android.notification.service.BaseNotificationProcessStarter
import ru.surfstudio.android.notification.ui.UiHandler
import javax.inject.Inject

class NotificationProcessStarter @Inject constructor(
        uiHandler: UiHandler,
        applicationContext: Context
) : BaseNotificationProcessStarter(uiHandler, applicationContext) {

    override fun getAppStartReceiverClassName() = AppStartReceiver::class.java.canonicalName
}