package ru.surfstudio.android.core.mvi.ui.provider

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event

/**
 * [EventProvider] для [Observable]
 */
class ObservableEventProvider<E : Event, T>(
        override val observable: Observable<T>,
        override val eventTransformer: (T) -> E
) : EventProvider<E, T>
