package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.OpenScreenEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ReactiveListRoute
import ru.surfstudio.android.core.ui.state.LifecycleStage

sealed class MainEvent : Event {
    data class OpenComplexList(
            override val route: ReactiveListRoute = ReactiveListRoute()
    ) : MainEvent(), OpenScreenEvent

    object OpenInputForm : MainEvent()
    object OpenSimpleList : MainEvent()
    data class MainLifecycle(override var stage: LifecycleStage) : MainEvent(), LifecycleEvent
}
