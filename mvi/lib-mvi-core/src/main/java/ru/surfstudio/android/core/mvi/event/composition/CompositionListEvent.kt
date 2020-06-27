package ru.surfstudio.android.core.mvi.event.composition

import ru.surfstudio.android.core.mvi.event.Event

/**
 * Event that nests other events within itself. Used as alternative to event inheritance.
 *
 * Events from the list can be processed with custom reusable middleware of type [E].
 *
 * This is similar to [CompositionEvent], but contains collection of events in it.
 */
interface CompositionListEvent<E : Event> : Event {
    var events: List<E>
}