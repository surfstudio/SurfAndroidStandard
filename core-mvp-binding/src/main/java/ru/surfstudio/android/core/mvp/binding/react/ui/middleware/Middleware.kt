package ru.surfstudio.android.core.mvp.binding.react.ui.middleware

import ru.surfstudio.android.core.mvp.binding.react.event.Event

/**
 * Базовая сущность Middleware в терминологии MVI.
 * Трансформирует входящий поток событий из EventHub, и направляет его обратно.
 *
 * Эта сущность используется для трансформации событий Ui-слоя, задействуя элементы сервисного слоя.
 * Например, для осуществления загрузки данных по нажатию на кнопку.
 */
interface Middleware<T : Event, InputStream, OutputStream> {

    fun transform(eventStream: InputStream): OutputStream

    fun flatMap(event: T): OutputStream
}