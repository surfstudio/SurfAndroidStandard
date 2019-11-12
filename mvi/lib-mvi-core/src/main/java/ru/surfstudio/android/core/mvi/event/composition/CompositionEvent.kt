package ru.surfstudio.android.core.mvi.event.composition

import ru.surfstudio.android.core.mvi.event.Event

/**
 * Событие, содержащее в себе другие события
 *
 * Служит для реализации операции наследования и обработки списка событий отдельным переиспользуемым middleware.
 */
interface CompositionEvent<E : Event> {
    var events: List<E>
}