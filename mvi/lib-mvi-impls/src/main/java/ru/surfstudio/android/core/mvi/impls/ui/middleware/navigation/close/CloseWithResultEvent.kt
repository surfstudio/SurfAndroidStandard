package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import java.io.Serializable

/**
 * Событие закрытия экрана с результатом
 */
interface CloseWithResultEvent<T : Serializable> : CloseScreenEvent {
    val route: SupportOnActivityResultRoute<T>
    val result: T
    val isSuccess: Boolean
}