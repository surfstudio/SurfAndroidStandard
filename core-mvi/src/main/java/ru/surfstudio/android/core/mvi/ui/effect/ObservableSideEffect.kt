package ru.surfstudio.android.core.mvi.ui.effect

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event

/**
 * [SideEffect] для [Observable]
 */
class ObservableSideEffect<E : Event, T>(
        override val observable: Observable<T>,
        override val eventTransformer: (T) -> E
) : SideEffect<E, T>
