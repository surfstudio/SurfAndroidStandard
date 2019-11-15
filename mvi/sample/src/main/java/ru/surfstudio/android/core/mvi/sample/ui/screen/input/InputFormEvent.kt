package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationComposition
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage

/**
 * События экрана [InputFormActivityView]
 */
sealed class InputFormEvent : Event {
    data class Lifecycle(override var stage: LifecycleStage) : LifecycleEvent, InputFormEvent()
    data class Navigation(override var events: List<NavigationEvent> = listOf()) : NavigationComposition, InputFormEvent()

    object SubmitClicked : InputFormEvent()
    data class InputChanged(val input: String) : InputFormEvent()
}