package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.mvp.binding.rx.response.type.Response

/**
 * Событие асинхронной загрузки данных.
 *
 * Содержит тип загрузки [Response],
 * меняющийся в зависимости от состояния загрузки данных.
 *
 * @property type тип загрузки данных (Loading, Data, Error)
 */
interface ResponseEvent<T> : Event {

    var type: Response<T>

    val hasData get() = type is Response.Data
}