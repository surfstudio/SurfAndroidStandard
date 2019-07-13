package ru.surfstudio.android.core.mvi.ui.provider

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event

/**
 * Класс, осуществляющий привязку всех значений класса [T] стейта к эвенту типа [E], после чего
 * все эвенты направляются в EventHub.
 *
 * Необходим для дальнейшего прокидывания значений в Middleware и осуществления маппинга.
 *
 * Следует использовать в случае, когда один стейт привязан к изменению значений нескольких других,
 * либо когда данные после преобразования в Reactor'е не должны отображаться на UI.
 */
interface EventProvider<E : Event, T> {
    val observable: Observable<T>
    val eventTransformer: (T) -> E

    fun observeEvents() = observable.map(eventTransformer::invoke)
}


