package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.mvp.model.LdsScreenModel
import ru.surfstudio.android.core.mvp.model.state.LoadState

/**
 * Модель экрана показа fcm-токена
 */
class FcmDebugScreenModel : LdsScreenModel() {
    var fcmToken: String? = null

    fun hasFcmToken(): Boolean = loadState != LoadState.EMPTY
}
