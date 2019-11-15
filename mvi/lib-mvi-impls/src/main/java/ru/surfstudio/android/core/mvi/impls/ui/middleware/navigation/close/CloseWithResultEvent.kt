package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import java.io.Serializable

/**
 * Closes screen with result
 *
 * @property route screen route
 * @property result screen result
 * @property isSuccess is result successful
 */
interface CloseWithResultEvent<T : Serializable> : CloseScreenEvent {
    val route: SupportOnActivityResultRoute<T>
    val result: T
    val isSuccess: Boolean
}