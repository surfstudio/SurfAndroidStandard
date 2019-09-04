package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close.CloseActivityEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close.CloseWithResultEvent
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.state.LifecycleStage

sealed class InputFormEvent : Event {
    object SubmitClicked : InputFormEvent()
    data class InputChanged(val input: String) : InputFormEvent()

    class InputFormClosed(
            override val route: SupportOnActivityResultRoute<String>,
            override val result: String,
            override val isSuccess: Boolean = true
    ) : CloseWithResultEvent<String>, InputFormEvent()

    data class InputFormLifecycle(override var stage: LifecycleStage) : LifecycleEvent, InputFormEvent()
}