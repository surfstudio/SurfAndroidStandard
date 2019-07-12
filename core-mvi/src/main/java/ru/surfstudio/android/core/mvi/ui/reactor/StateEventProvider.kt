package ru.surfstudio.android.core.mvi.ui.reactor

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * Класс, осуществляющий привязку всех значений класса [T] стейта к эвенту типа [E].
 *
 * Необходим для дальнейшего прокидывания значений в Middleware и осуществления маппинга.
 *
 * Следует использовать в случае, когда один стейт привязан к изменению значений нескольких других,
 * либо когда логика
 */
class StateEventProvider<E : Event, T>(
        val state: State<T>,
        val eventTransformer: (T) -> E
)