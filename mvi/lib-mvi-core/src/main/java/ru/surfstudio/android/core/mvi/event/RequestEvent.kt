package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request

/**
 * Событие асинхронной загрузки данных.
 *
 * Содержит тип загрузки [Request],
 * меняющийся в зависимости от состояния загрузки данных.
 *
 * @property type тип загрузки данных (Loading, Success, Error)
 */
interface RequestEvent<T> : Event {

    var type: Request<T>

    val hasData get() = type is Request.Success
}