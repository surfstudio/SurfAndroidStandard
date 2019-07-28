package ru.surfstudio.android.core.mvi.ui.reactor

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter

/**
 * Класс, реагирующий на событие изменением текущего состояния View.
 *
 * Чтобы не возвращаться к хранению всего состояния экрана в одном классе, мы не создаем каждый раз новое состояние,
 * а изменяем его, и даем View отреагировать на это с помощью Rx.
 */
interface Reactor<E : Event, H> : StateEmitter {

    fun react(holder: H, event: E)
}