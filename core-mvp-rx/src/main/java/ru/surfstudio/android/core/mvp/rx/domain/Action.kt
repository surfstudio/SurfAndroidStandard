package ru.surfstudio.android.core.mvp.rx.domain

import com.jakewharton.rxrelay2.PublishRelay

/**
 * Rx-обертка над действиями пользователя
 * За отправку событий отвечает View
 * Подписка на события происходит только внутри Presenter
 */
class Action<T> {
    internal val relay = PublishRelay.create<T>().toSerialized()

    val consumer get() = relay.asConsumer()

    /**
     * Получение нового значения и оповещение подписчиков
     *
     * @param newValue новое значение
     */
    fun accept(newValue: T) { relay.accept(newValue) }
}

/**
 * Получение нового значения и оповещение подписчиков
 *
 * Для пустого [Action] параметры не требуются
 */
fun Action<Unit>.accept() = accept(Unit)