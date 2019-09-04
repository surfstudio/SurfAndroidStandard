package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.OpenScreenEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.ScreenResultEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormActivityRoute
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ReactiveListRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

sealed class MainEvent : Event {
    data class OpenComplexList(
            override val route: ReactiveListRoute = ReactiveListRoute()
    ) : MainEvent(), OpenScreenEvent

    data class OpenInputForm(
            override val route: InputFormActivityRoute = InputFormActivityRoute()
    ) : MainEvent(), OpenScreenEvent

    data class InputFormResult(
            override var result: ScreenResult<String> = ScreenResult(false, EMPTY_STRING)
    ) : MainEvent(), ScreenResultEvent<String>

    object OpenSimpleList : MainEvent()
    data class MainLifecycle(override var stage: LifecycleStage) : MainEvent(), LifecycleEvent
}
