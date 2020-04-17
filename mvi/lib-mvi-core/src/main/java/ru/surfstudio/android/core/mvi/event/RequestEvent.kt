package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.mvp.binding.rx.request.Request

/**
 * Событие асинхронной загрузки данных.
 *
 * Содержит тип загрузки данных [Request].
 */
interface RequestEvent<T> : Event {

    /**
     * Тип загрузки данных (Loading, Success, Error).
     */
    val request: Request<T>

    /**
     * Флаг, определяющий, выполняется ли запрос в данный момент.
     */
    val isLoading: Boolean get() = request.isLoading

    /**
     * Флаг, определяющий, выполнился ли запрос успешно.
     */
    val isSuccess: Boolean get() = request.isSuccess

    /**
     * Флаг, определяющий, выполнился ли запрос с ошибкой.
     */
    val isError: Boolean get() = request.isError
}