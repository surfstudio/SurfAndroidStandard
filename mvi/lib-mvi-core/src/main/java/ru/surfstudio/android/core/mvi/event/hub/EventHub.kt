package ru.surfstudio.android.core.mvi.event.hub

import ru.surfstudio.android.core.mvi.event.Event

/**
 * Базовый типизированный EventHub.
 * Служит для получения и рассылки сообщений, является своеобразной шиной между вью и данными.
 *
 * @param T         тип события, которым типизирован Hub
 * @param EventStream    тип потока событий, которые испускает Hub
 */
interface EventHub<T : Event, EventStream> {

    /**
     * Отправка события
     */
    fun emit(event: T)

    /**
     * Поток отправляемых событий
     */
    fun observe(): EventStream
}