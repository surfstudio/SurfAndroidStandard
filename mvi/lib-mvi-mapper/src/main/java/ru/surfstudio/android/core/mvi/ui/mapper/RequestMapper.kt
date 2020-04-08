package ru.surfstudio.android.core.mvi.ui.mapper

import ru.surfstudio.android.core.mvp.binding.rx.request.data.Loading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request

/**
 * Предназначен для удобного и максимально контролируемого
 * преобразования [Request] любого типа в [RequestUi] любого типа,
 * а также для достижения наиболее прозрачной и понятной обработки запроса
 * посредством использования паттерна `Builder`.
 *
 * Позволяет переиспользовать логику уже созданных мапперов данных/ошибки/состояния загрузки,
 * а также создавать свои специфичные мапперы.
 *
 * Сущность следует создавать и использовать через билдеры компаньона.
 *
 * @param T Тип данных запроса.
 * @param D Тип ui-данных.
 * */
class RequestMapper<T, D> private constructor(
        private val request: Request<T>,
        private val data: D?,
        private val loading: Loading?,
        private val isErrorHandled: Boolean
) {

    /**
     * Маппер типа данных запроса.
     * Используется для трансформации `Request<T1>` в `Request<T2>`.
     * Получает в качестве аргументов: исходные данные запроса.
     *
     * @param R Трансформированные данные запроса.
     * */
    fun <R> mapRequest(mapper: RequestTypeMapper<T, R>): RequestMapper<R, D> {
        val newRequest = request.map(mapper)
        return produceNewRequestMapper(newRequest, data)
    }

    /**
     * Замапить данные запроса.
     * Получает в качестве аргументов: запрос и ui-данные.
     *
     * @param R Тип замапенных ui-данных.
     * */
    fun <R> mapData(mapper: RequestDataMapper<T, D, R>): RequestMapper<T, R> {
        val newData = mapper(request, data)
        return produceNewRequestMapper(request, newData)
    }

    /**
     * Замапить состояние загрузки запроса.
     * Получает в качестве аргументов: запрос и ui-данные.
     * */
    fun mapLoading(mapper: RequestLoadingMapper<T, D>): RequestMapper<T, D> {
        val newLoading = mapper(request, data)
        return produceNewRequestMapper(request, data, loading = newLoading)
    }

    /**
     * Обработать ошибку запроса.
     *
     * `RequestErrorHandler`:
     * Получает в качестве аргументов: ошибку, ui-данные и состояния загрузки.
     * Возвращает: была ли обработана ошибка?
     * */
    fun handleError(handler: RequestErrorHandler<D>): RequestMapper<T, D> {
        if (isErrorHandled) return this
        val isHandled = handler(request.errorOrNull, data, loading)
        return produceNewRequestMapper(request, data, isErrorHandled = isHandled)
    }

    /**
     * Обработать специфичную ошибку запроса.
     * Получает в качестве аргументов: ошибку, ui-данные и состояния загрузки.
     * Возвращает: была ли обработана ошибка?
     *
     * @param E Тип отлавливаемой ошибки.
     * */
    @Suppress("UNCHECKED_CAST")
    fun <E : Throwable> handleSpecificError(handler: RequestErrorHandler<D>): RequestMapper<T, D> {
        val error = request.errorOrNull as? E
        return when {
            !isErrorHandled && error is E -> {
                val isHandled = handler(request.errorOrNull, data, loading)
                produceNewRequestMapper(request, data, isErrorHandled = isHandled)
            }
            else -> this
        }
    }

    /**
     * "Среагировать" на любое изменение состояния выполнения запроса.
     * Получает в качестве аргументов: запрос, ui-данные и состояние загрузки.
     * */
    fun react(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { reactor(request, data, loading) }

    /**
     * "Среагировать" при успехе выполнения запроса.
     * Получает в качестве аргументов: запрос, ui-данные и состояние загрузки.
     * */
    fun reactOnSuccess(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { if (request.isSuccess) reactor(request, data, loading) }

    /**
     * "Среагировать" при успехе выполнения запроса.
     * Получает в качестве аргументов: ui-данные.
     * */
    fun reactOnSuccess(reactor: RequestDataReactor<D>): RequestMapper<T, D> =
            this.also { if (request.isSuccess) reactor(data) }

    /**
     * "Среагировать" при ошибке выполнения запроса.
     * Получает в качестве аргументов: запрос, ui-данные и состояние загрузки.
     * */
    fun reactOnError(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { if (request.isError) reactor(request, data, loading) }

    /**
     * "Среагировать" при ошибке выполнения запроса.
     * Получает в качестве аргументов: ошибку.
     * */
    fun reactOnError(reactor: RequestErrorReactor): RequestMapper<T, D> =
            this.also { if (request.isError) reactor(request.errorOrNull) }

    /**
     * "Среагировать" при загрузке запроса.
     * Получает в качестве аргументов: запрос, ui-данные и состояние загрузки.
     * */
    fun reactOnLoading(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { if (request.isLoading) reactor(request, data, loading) }

    /**
     * "Среагировать" при загрузке запроса.
     * Получает в качестве аргументов: состояние загрузки.
     * */
    fun reactOnLoading(reactor: RequestLoadingReactor): RequestMapper<T, D> =
            this.also { if (request.isLoading) reactor(loading) }

    /**
     * "Собрать" результат выполнения маппинга в [RequestUi].
     * */
    fun build(): RequestUi<D> = RequestUi(data, loading, request.errorOrNull)

    /**
     * Используется для быстрого создания нового инстанса [RequestMapper]'а.
     *
     * @param T Тип данных запроса.
     * @param D Тип ui-данных.
     * */
    private fun <T, D> produceNewRequestMapper(
            request: Request<T>,
            data: D?,
            loading: Loading? = this.loading,
            isErrorHandled: Boolean = this.isErrorHandled
    ): RequestMapper<T, D> {
        return RequestMapper(request, data, loading, isErrorHandled)
    }

    companion object {

        /**
         * Билдер для запроса без ui-данных.
         *
         * @param T Тип данных запроса.
         * */
        fun <T> builder(request: Request<T>) =
                RequestMapper<T, T>(request, null, null, false)

        /**
         * Билдер для запроса с ui-данными, упакованными в [RequestUi].
         *
         * @param T Тип данных запроса.
         * @param D Тип ui-данных.
         * */
        fun <T, D> builder(request: Request<T>, requestUi: RequestUi<D>) =
                RequestMapper(request, requestUi.data, null, false)

        /**
         * Билдер для запроса с ui-данными, которые хранятся в выделенных переменных.
         *
         * @param T Тип данных запроса.
         * @param D Тип ui-данных.
         * */
        fun <T, D> builder(request: Request<T>, data: D?) =
                RequestMapper(request, data, null, false)
    }
}