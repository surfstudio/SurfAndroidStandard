package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.mvp.binding.rx.request.Request

/**
 * Событие асинхронной загрузки данных.
 *
 * Содержит тип загрузки [Request], меняющийся в зависимости от состояния загрузки данных.
 */
interface RequestEvent<T> : Event {

    /**
     * Тип загрузки данных (Loading, Success, Error).
     */
    val request: Request<T>
}