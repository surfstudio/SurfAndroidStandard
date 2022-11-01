package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list.controller.StepperData
import ru.surfstudio.android.core.ui.state.LifecycleStage

/**
 * События экрана [SimpleListActivityView]
 */
sealed class SimpleListEvent : Event {
    data class Lifecycle(override var stage: LifecycleStage) : SimpleListEvent(), LifecycleEvent

    data class StepperClicked(val id: Int) : SimpleListEvent()
    data class ListLoaded(val list: List<StepperData>) : SimpleListEvent()
}