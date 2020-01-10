package ru.surfstudio.android.core.mvp.binding.rx.builders

import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator

/**
 * Интерфейс builder'а для UI с поддержкой закрытия экрана при отсутствии интернета.
 */
interface UiBuilderFinish {
    val activityNavigator: ActivityNavigator

    /**
     * Закрытие текущей activity
     */
    fun finish() {
        activityNavigator.finishCurrent()
    }
}