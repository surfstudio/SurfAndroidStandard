package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель экрана показа fcm-токена
 */
class FcmDebugScreenModel : ScreenModel() {

    var fcmToken: String? = null

    fun hasFcmToken(): Boolean = !fcmToken.isNullOrEmpty()
}
