package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.ExperimentalFeature
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import java.io.Serializable

/**
 * Событие получения результата работы экрана.
 * Используется в механизме Middleware Navigation.
 */
@ExperimentalFeature
interface ScreenResultEvent<T : Serializable> : Event {
    var result: ScreenResult<T>
}
