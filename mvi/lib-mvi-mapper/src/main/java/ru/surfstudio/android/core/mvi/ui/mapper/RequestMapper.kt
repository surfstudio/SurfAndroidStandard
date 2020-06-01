/*
  Copyright (c) 2020, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package ru.surfstudio.android.core.mvi.ui.mapper

import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.mvp.binding.rx.request.data.Loading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import kotlin.reflect.KClass

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
     * ## RequestTypeMapper
     * **Маппер типа данных запроса.**
     *
     * Используется для трансформации `Request<T>` в `Request<R>`.
     *
     * **Получает в качестве аргументов**: исходные данные запроса.
     *
     * **Возвращает**: новые исходные данных запроса.
     *
     * @param R Трансформированные данные запроса.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun <R> mapRequest(mapper: RequestTypeMapper<T, R>): RequestMapper<R, D> {
        val newRequest = request.map(mapper)
        return produceNewRequestMapper(newRequest, data)
    }

    /**
     * ## RequestDataMapper
     * **Маппер типа данных запроса.**
     *
     * Используется для трансформации `Request<T>` в `Request<R>`.
     *
     * **Получает в качестве аргументов**: исходные данные запроса.
     *
     * **Возвращает**: новые исходные данных запроса.
     *
     * @param R Трансформированные данные запроса.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun <R> mapData(mapper: RequestDataMapper<T, D, R>): RequestMapper<T, R> {
        val newData = mapper(request, data)
        return produceNewRequestMapper(request, newData)
    }

    /**
     * ## RequestLoadingMapper
     * **Маппер состояния загрузки запроса.**
     *
     * Используется для репрезентации состояния выполнения запроса на UI.
     *
     * **Получает в качестве аргументов**: запрос и ui-данные.
     *
     * **Возвращает**: состояние загрузки запроса.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun mapLoading(mapper: RequestLoadingMapper<T, D>): RequestMapper<T, D> {
        val newLoading = mapper(request, data)
        return produceNewRequestMapper(request, data, loading = newLoading)
    }

    /**
     * ## RequestErrorHandler
     * **Обработчик ошибки запроса.**
     *
     * Используется для обработки возникающих в процессе выполнения запроса ошибок.
     *
     * Применяется паттерн `Chain of Responsibility`, т.е. каждый обработчик по-очередно
     * выполняет обработку ошибки и возвращает результат: была ли обработана ошибка.
     *
     * Если ошибка не была обработана, то она будет передана в следующие обработчики (если таковые есть)
     * пока ошибка не будет обработана, или же не закончатся обработчики.
     *
     * **Получает в качестве аргументов**: ошибку, ui-данные и состояние загрузки.
     *
     * **Возвращает**: была ли обработана ошибка?
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun handleError(handler: RequestErrorHandler<D>): RequestMapper<T, D> {
        if (!request.isError || isErrorHandled) return this
        val isHandled = handler(request.getErrorOrNull(), data, loading)
        return produceNewRequestMapper(request, data, isErrorHandled = isHandled)
    }

    /**
     * Обрабатывает только ошибки типа [E].
     *
     * ## RequestErrorHandler
     * **Обработчик ошибки запроса.**
     *
     * Используется для обработки возникающих в процессе выполнения запроса ошибок.
     *
     * Применяется паттерн `Chain of Responsibility`, т.е. каждый обработчик по-очередно
     * выполняет обработку ошибки и возвращает результат: была ли обработана ошибка.
     *
     * Если ошибка не была обработана, то она будет передана в следующие обработчики (если таковые есть)
     * пока ошибка не будет обработана, или же не закончатся обработчики.
     *
     * **Получает в качестве аргументов**: ошибку, ui-данные и состояние загрузки.
     *
     * **Возвращает**: была ли обработана ошибка?
     *
     * @param E Тип обрабатываемой ошибки.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    inline fun <reified E : Throwable> handleSpecificError(noinline handler: RequestSpecificErrorHandler<E, D>): RequestMapper<T, D> {
        val errorClass = E::class
        return handleSpecificError(errorClass, handler)
    }

    /**
     * Обрабатывает только ошибки типа [E].
     *
     * ## RequestErrorHandler
     * **Обработчик ошибки запроса.**
     *
     * Используется для обработки возникающих в процессе выполнения запроса ошибок.
     *
     * Применяется паттерн `Chain of Responsibility`, т.е. каждый обработчик по-очередно
     * выполняет обработку ошибки и возвращает результат: была ли обработана ошибка.
     *
     * Если ошибка не была обработана, то она будет передана в следующие обработчики (если таковые есть)
     * пока ошибка не будет обработана, или же не закончатся обработчики.
     *
     * **Получает в качестве аргументов**: ошибку, ui-данные и состояние загрузки.
     *
     * **Возвращает**: была ли обработана ошибка?
     *
     * @param errorClass класс для обрабатываемой ошибки.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun <E : Throwable> handleSpecificError(errorClass: KClass<E>, handler: RequestSpecificErrorHandler<E, D>): RequestMapper<T, D> {
        val error = request.getErrorOrNull()
        val isSpecificError = error?.let { it::class == errorClass } ?: false
        return when {
            !isErrorHandled && isSpecificError -> {
                val castedError: E = errorClass.java.cast(error)!!
                val isHandled = handler(castedError, data, loading)
                produceNewRequestMapper(request, data, isErrorHandled = isHandled)
            }
            else -> this
        }
    }

    /**
     * ## RequestReactor
     * **Реактор на изменение состояния жизненного цикла запроса.**
     *
     * Используется для реагирования на изменения состояния запроса, такие как:
     * `Загрузка`, `Ошибка` и `Успех`.
     *
     * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
     *
     * **Получает в качестве аргументов**: запрос, ui-данные и состояние загрузки.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun react(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { reactor(request, data, loading) }

    /**
     * Реагирует только на события успешного выполнения запроса.
     *
     * ## RequestReactor
     * **Реактор на изменение состояния жизненного цикла запроса.**
     *
     * Используется для реагирования на изменения состояния запроса, такие как:
     * `Загрузка`, `Ошибка` и `Успех`.
     *
     * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
     *
     * **Получает в качестве аргументов**: запрос, ui-данные и состояние загрузки.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun reactOnSuccess(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { if (request.isSuccess) reactor(request, data, loading) }

    /**
     * ## RequestDataReactor
     * **Реактор на успешно завершившийся запрос.**
     *
     * Является урезанной версией [RequestReactor], для более лаконичного использования лямбд
     * (один аргумент против трех).
     *
     * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
     *
     * **Получает в качестве аргументов**: ui-данные.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun reactOnSuccess(reactor: RequestDataReactor<D>): RequestMapper<T, D> =
            this.also { if (request.isSuccess) reactor(data) }

    /**
     * Реагирует только на события неудачного выполнения запроса.
     *
     * ## RequestReactor
     * **Реактор на изменение состояния жизненного цикла запроса.**
     *
     * Используется для реагирования на изменения состояния запроса, такие как:
     * `Загрузка`, `Ошибка` и `Успех`.
     *
     * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
     *
     * **Получает в качестве аргументов**: запрос, ui-данные и состояние загрузки.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun reactOnError(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { if (request.isError) reactor(request, data, loading) }

    /**
     * ## RequestErrorReactor
     * **Реактор на неудачно завершившийся запрос.**
     *
     * Является урезанной версией [RequestReactor], для более лаконичного использования лямбд
     * (один аргумент против трех).
     *
     * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
     *
     * **Получает в качестве аргументов**: ошибку.
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun reactOnError(reactor: RequestErrorReactor): RequestMapper<T, D> =
            this.also { if (request.isError) reactor(request.getErrorOrNull()) }

    /**
     * Реагирует только на события загрузки запроса.
     *
     * ## RequestReactor
     * **Реактор на изменение состояния жизненного цикла запроса.**
     *
     * Используется для реагирования на изменения состояния запроса, такие как:
     * `Загрузка`, `Ошибка` и `Успех`.
     *
     * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
     *
     * **Получает в качестве аргументов**: запрос, ui-данные и состояние загрузки.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun reactOnLoading(reactor: RequestReactor<T, D>): RequestMapper<T, D> =
            this.also { if (request.isLoading) reactor(request, data, loading) }

    /**
     * ## RequestLoadingReactor
     * **Реактор на загрузку запроса.**
     *
     * Является урезанной версией [RequestReactor], для более лаконичного использования лямбд
     * (один аргумент против трех).
     *
     * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
     *
     * **Получает в качестве аргументов**: состояние загрузки.
     *
     * @return Новый экземпляр `RequestMapper`.
     * */
    fun reactOnLoading(reactor: RequestLoadingReactor): RequestMapper<T, D> =
            this.also { if (request.isLoading) reactor(loading) }

    /**
     * "Собрать" результат выполнения маппинга в [RequestUi].
     * */
    fun build(): RequestUi<D> = RequestUi(data, loading, request.getErrorOrNull())

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