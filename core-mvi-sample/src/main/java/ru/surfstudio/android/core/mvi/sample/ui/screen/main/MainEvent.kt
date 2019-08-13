package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage

sealed class MainEvent : Event {
    object OpenComplexList : MainEvent()
    object OpenInputForm : MainEvent()
    object OpenSimpleList : MainEvent()
    class Lifecycle(override var stage: LifecycleStage) : MainEvent(), LifecycleEvent
}
