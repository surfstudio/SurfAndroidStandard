package ru.surfstudio.standard.app_injector.ui.notification

import com.google.firebase.messaging.FirebaseMessagingService
import ru.surfstudio.android.logger.Logger

/**
 * Сервис для обработки пришедших пуш-уведомлений от Firebase.
 *
 * Срабатывает только если приложение не в фоне.
 * Иначе при клике на пуш происходит открытие LAUNCHER активити
 */
class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)
        Logger.i("Новый Firebase токен: $newToken")
    }
}