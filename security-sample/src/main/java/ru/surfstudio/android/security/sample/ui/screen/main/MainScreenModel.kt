package ru.surfstudio.android.security.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель главного экрана
 */
class MainScreenModel : ScreenModel() {
    companion object {
        const val MAX_ENTER_PIN_ATTEMPT_COUNT = 3
    }
}
