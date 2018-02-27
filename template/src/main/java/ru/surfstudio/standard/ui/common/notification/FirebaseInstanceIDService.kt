package ru.surfstudio.standard.ui.common.notification

import com.google.firebase.iid.FirebaseInstanceIdService
import ru.surfstudio.android.logger.Logger

/**
 * Сервис, который обрабатывает обновление токена для пуш уведомлений
 */
class FirebaseInstanceIDService : FirebaseInstanceIdService() {


    override fun onTokenRefresh() {
        super.onTokenRefresh()

        Logger.i("Firebase Update fcm")
    }
}
