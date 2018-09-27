package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.mvp.model.LdsScreenModel

/**
 * Модель экрана показа fcm-токена
 */
class FcmDebugScreenModel : LdsScreenModel() {
    var fcmToken: String? = null
}
