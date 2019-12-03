package ru.surfstudio.android.core.mvi.ui.reactor

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter

/**
 * Класс, реагирующий на событие изменением текущего состояния View.
 *
 * Чтобы не возвращаться к хранению всего состояния экрана в одном классе, мы не создаем каждый раз новое состояние,
 * а изменяем его, и даем View отреагировать на это с помощью Rx.
 *
 * [E] - тип события, на которое реагирует реактор
 * [H] - тип StateHolder (хранителя состояния)
 */
interface Reactor<E : Event, H> : StateEmitter {

    /**
     * Реакция на событие
     *
     * @param sh StateHolder, который содержит логику смены состояния
     * @param event событие, на которое реагирует reactor
     */
    fun react(sh: H, event: E)
}