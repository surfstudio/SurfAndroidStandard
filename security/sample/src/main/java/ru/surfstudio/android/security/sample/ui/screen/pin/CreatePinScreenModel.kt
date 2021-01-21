package ru.surfstudio.android.security.sample.ui.screen.pin

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Модель экрана ввода pin-кода
 */
class CreatePinScreenModel : ScreenModel() {
    var isBiometricsAvailable: Boolean = false
    var encryptedPin: String = EMPTY_STRING
}
