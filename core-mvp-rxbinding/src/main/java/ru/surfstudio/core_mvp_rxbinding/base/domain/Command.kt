package ru.surfstudio.core_mvp_rxbinding.base.domain

import com.jakewharton.rxrelay2.PublishRelay

/**
 * Rx-обертка над командами для View
 * Не является состоянием [State], так как команда должна быть показана только один раз
 *
 * За отправку событий отвечает Presenter
 * Подписывается на события View
 */
class Command<T> {
    internal val relay = PublishRelay.create<T>().toSerialized()

    val consumer get() = relay.asConsumer()
    val observable get() = relay.asObservable()

    /**
     * Получение нового значения и оповещение подписчиков
     *
     * @param newValue новое значение
     */
    fun accept(newValue: T) { relay.accept(newValue) }
}