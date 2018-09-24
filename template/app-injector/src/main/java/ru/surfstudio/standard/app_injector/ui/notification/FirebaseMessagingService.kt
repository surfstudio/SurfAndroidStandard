package ru.surfstudio.standard.app_injector.ui.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.NotificationCenter
import ru.surfstudio.standard.app_injector.AppInjector
import ru.surfstudio.standard.i_debug.DebugInteractor
import javax.inject.Inject

/**
 * Сервис для обработки пришедших пуш-уведомлений от Firebase.
 *
 * Срабатывает только если приложение не в фоне.
 * Иначе при клике на пуш происходит открытие LAUNCHER активити
 */
class FirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var debugInteractor: DebugInteractor

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        AppInjector.appComponent.inject(this)
        Logger.i("Новый Firebase токен: $newToken")
        debugInteractor.saveFcmToken(newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Logger.i("Получено push-уведомление: " +
                "title = [${remoteMessage?.notification?.title}], " +
                "body = [${remoteMessage?.notification?.body}], " +
                "data = [${remoteMessage?.data}]")

        remoteMessage?.let {
            NotificationCenter.onReceiveMessage(this,
                    it.notification?.title ?: "",
                    it.notification?.body ?: "",
                    it.data)
        }
    }
}