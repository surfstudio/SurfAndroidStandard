package ru.surfstudio.android.core.mvi.ui.middleware

import ru.surfstudio.android.core.mvi.event.Event

/**
 * Базовая сущность Middleware в терминологии MVI.
 * Трансформирует входящий поток событий из EventHub, и направляет его обратно.
 *
 * Эта сущность используется для трансформации событий Ui-слоя, задействуя элементы сервисного слоя.
 * Например, для осуществления загрузки данных по нажатию на кнопку.
 *
 * @param T             тип события, которым типизирован Hub
 * @param InputStream   тип входного потока событий
 * @param OutputStream  тип выходящего потока событий
 */
interface Middleware<T : Event, InputStream, OutputStream> {

    fun transform(eventStream: InputStream): OutputStream
}