package ru.surfstudio.core_mvp_rxbinding.base.domain

import com.jakewharton.rxrelay2.PublishRelay

/**
 * Rx-обертка над действиями пользователя
 * За отправку событий отвечает View
 * Подписка на события происходит только внутри Presenter
 */
class Action<T> {
    internal val relay = PublishRelay.create<T>().toSerialized()

    val consumer get() = relay.asConsumer()

    fun accept(newValue: T) { relay.accept(newValue) }
}

/**
 *
 */
fun Action<Unit>.accept() = accept(Unit)