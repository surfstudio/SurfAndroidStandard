package ru.surfstudio.android.core.mvp.binding.rx.request

/**
 * Тип загрузки данных уровня Interactor.
 *
 * Отражает цикл асинхронной загрузки данных:
 *
 * 1. Получение данных начинается с состояния загрузки [Request.Loading],
 * 2. После этого либо приходят данные [Request.Success],
 * 3. Либо приходит ошибка загрузки данных [Request.Error]
 */
sealed class Request<T> {

    class Loading<T> : Request<T>()
    data class Success<T>(val data: T) : Request<T>()
    data class Error<T>(val error: Throwable) : Request<T>()

    /**
     * Флаг, определяющий, выполняется ли запрос в данный момент.
     */
    val isLoading: Boolean get() = this is Loading

    /**
     * Флаг, определяющий, выполнился ли запрос успешно.
     */
    val isSuccess: Boolean get() = this is Success

    /**
     * Флаг, определяющий, выполнился ли запрос с ошибкой.
     */
    val isError: Boolean get() = this is Error

    /**
     * Извлечение данных, полученных в результате выполнения запроса.
     */
    fun extractData(): T = (this as Success).data

    /**
     * Безопасное извлечение данных, полученных в результате выполнения запроса.
     * В случае отсутствия данных будет возвращен null.
     */
    fun extractDataOrNull(): T? = (this as? Success)?.data

    /**
     * Извлечение ошибки, полученной в результате выполнения запроса.
     */
    fun extractError(): Throwable = (this as Error).error

    /**
     * Безопасное извлечение ошибки, полученной в результате выполнения запроса.
     * В случае отсутствия ошибки будет возвращен null.
     */
    fun extractErrorOrNull(): Throwable? = (this as? Error)?.error

    /**
     * Трансформация Request<T> в Request<R>.
     *
     * @param mapper функция трансформации данных запроса.
     */
    fun <R> map(mapper: (T) -> R): Request<R> {
        return when (this) {
            is Success -> Success(mapper(data))
            is Error -> Error(error)
            is Loading -> Loading()
        }
    }
}