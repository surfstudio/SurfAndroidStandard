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

    /**
     * Извлечение данных, полученных в результате выполнения запроса.
     *
     * **Будет выброшено исключение,
     * если данные извлекаются из экземпляра [Request], отличного от [Request.Success].**
     */
    fun getData(): T = request.getData()

    /**
     * Безопасное извлечение данных, полученных в результате выполнения запроса.
     * В случае отсутствия данных будет возвращен null.
     */
    fun getDataOrNull(): T? = request.getDataOrNull()

    /**
     * Извлечение ошибки, полученной в результате выполнения запроса.
     *
     * **Будет выброшено исключение,
     * если ошибка извлекается из экземпляра [Request], отличного от [Request.Error].**
     */
    fun getError(): Throwable = request.getError()

    /**
     * Безопасное извлечение ошибки, полученной в результате выполнения запроса.
     * В случае отсутствия ошибки будет возвращен null.
     */
    fun getErrorOrNull(): Throwable? = request.getErrorOrNull()
}