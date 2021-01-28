package ru.surfstudio.standard.ui.mvi.composition

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent

/**
 * Событие композиции, в котором не нужен список событий: достаточно передавать только одно событие.
 */
interface SingleEventComposition<C : Event> : CompositionEvent<C> {

    var event: C

    override var events: List<C>
        get() = listOf(event)
        set(value) {
            event = value.first()
        }
}
