package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close

import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import java.io.Serializable

interface CloseWithResultEvent<T : Serializable> : CloseScreenEvent {
    val route: SupportOnActivityResultRoute<T>
    val result: T
    val isSuccess: Boolean
}