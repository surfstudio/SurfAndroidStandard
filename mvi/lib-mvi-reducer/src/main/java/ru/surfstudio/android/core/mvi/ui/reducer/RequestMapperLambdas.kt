package ru.surfstudio.android.core.mvi.ui.reducer

import ru.surfstudio.android.core.mvp.binding.rx.request.data.Loading
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request

/**
 * Маппер типа данных запроса.
 * Используется для трансформации `Request<T1>` в `Request<T2>`.
 * Получает в качестве аргументов: исходные данные запроса.
 *
 * @param T Исходные данные запроса.
 * @param R Трансформированные данные запроса.
 * */
typealias RequestTypeMapper<T, R> = (data: T) -> R

/**
 * Маппер данных запроса.
 * Используется для комбинирования данных запроса и ui-данных.
 * Получает в качестве аргументов: запрос и ui-данные.
 *
 * @param T Тип данных запроса.
 * @param D Тип ui-данных.
 * @param R Тип замапенных ui-данных.
 * */
typealias RequestDataMapper<T, D, R> = (request: Request<T>, data: D?) -> R?

/**
 * Маппер состояния загрузки запроса.
 * Получает в качестве аргументов: запрос и ui-данные.
 *
 * @param T Тип данных запроса.
 * @param D Тип ui-данных.
 * */
typealias RequestLoadingMapper<T, D> = (request: Request<T>, data: D?) -> Loading?

/**
 * Обработчик ошибки запроса.
 * Получает в качестве аргументов: ошибку, ui-данные и состояния загрузки.
 * Возвращает: была ли обработана ошибка?
 *
 * @param T Тип данных запроса.
 * */
typealias RequestErrorHandler<T> = (error: Throwable?, data: T?, loading: Loading?) -> Boolean

/**
 * Реактор изменения состояния запроса.
 * Получает в качестве аргументов: запрос, ui-данные и состояние загрузки.
 *
 * @param T Тип данных запроса.
 * @param D Тип ui-данных.
 * */
typealias RequestReactor<T, D> = (request: Request<T>, data: D?, loading: Loading?) -> Unit

/**
 * Реактор изменения состояния запроса.
 * Получает в качестве аргументов: ui-данные.
 *
 * @param D Тип ui-данных.
 * */
typealias RequestDataReactor<D> = (data: D?) -> Unit

/**
 * Реактор изменения состояния запроса.
 * Получает в качестве аргументов: ошибку.
 * */
typealias RequestErrorReactor = (error: Throwable?) -> Unit

/**
 * Реактор изменения состояния запроса.
 * Получает в качестве аргументов: состояние загрузки.
 * */
typealias RequestLoadingReactor = (loading: Loading?) -> Unit