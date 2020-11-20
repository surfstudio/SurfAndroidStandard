package ru.surfstudio.android.core.mvp.binding.rx.builders

import ru.surfstudio.android.core.ui.navigation.IActivityNavigator

/**
 * Интерфейс builder'а для UI с поддержкой закрытия экрана при отсутствии интернета.
 */
interface UiBuilderFinish {
    val activityNavigator: IActivityNavigator

    /**
     * Закрытие текущей activity
     */
    fun finish() {
        activityNavigator.finishCurrent()
    }
}