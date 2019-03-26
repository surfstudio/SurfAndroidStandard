package ru.surfstudio.standard.application.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.standard.application.app.di.AppInjector
import ru.surfstudio.standard.i_push_notification.storage.FcmStorage
import javax.inject.Inject

/**
 * Сервис для обработки пришедших пуш-уведомлений от Firebase.
 *
 * Срабатывает только если приложение не в фоне.
 * Иначе при клике на пуш происходит открытие LAUNCHER активити
 */
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmStorage: FcmStorage

    @Inject
    lateinit var pushHandler: PushHandler
    override fun onCreate() {
        super.onCreate()
        DaggerFirebaseServiceComponent.builder()
                .appComponent(AppInjector.appComponent)
                .build()
                .inject(this)
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Logger.i("Новый Firebase токен: $newToken")
        fcmStorage.fcmToken = newToken
        //todo отправить новый токен на сервер для получения push-уведомлений
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Logger.i("Получено push-уведомление: " +
                "title = [${remoteMessage?.notification?.title}], " +
                "body = [${remoteMessage?.notification?.body}], " +
                "data = [${remoteMessage?.data}]")

        remoteMessage?.let {
            pushHandler.handleMessage(this,
                    it.messageId?.hashCode() ?: -1,
                    it.notification?.title ?: "",
                    it.notification?.body ?: "",
                    it.data)
        }
    }
}