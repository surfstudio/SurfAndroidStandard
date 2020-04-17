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

/**
 * **Маппер типа данных запроса.**
 *
 * Используется для трансформации `Request<T>` в `Request<R>`.
 *
 * **Получает в качестве аргументов**: исходные данные запроса.
 *
 * **Возвращает**: новые исходные данных запроса.
 *
 * @param T Исходные данные запроса.
 * @param R Трансформированные данные запроса.
 * */
typealias RequestTypeMapper<T, R> = (data: T) -> R

/**
 * **Маппер данных запроса.**
 *
 * Используется для комбинирования данных запроса и ui-данных.
 *
 * **Получает в качестве аргументов**: запрос и ui-данные.
 *
 * **Возвращает**: новые ui-данные.
 *
 * @param T Тип данных запроса.
 * @param D Тип ui-данных.
 * @param R Тип новых ui-данных.
 * */
typealias RequestDataMapper<T, D, R> = (request: Request<T>, data: D?) -> R?

/**
 * **Маппер состояния загрузки запроса.**
 *
 * Используется для репрезентации состояния выполнения запроса на UI.
 *
 * **Получает в качестве аргументов**: запрос и ui-данные.
 *
 * **Возвращает**: состояние загрузки запроса.
 *
 * @param T Тип данных запроса.
 * @param D Тип ui-данных.
 * */
typealias RequestLoadingMapper<T, D> = (request: Request<T>, data: D?) -> Loading?

/**
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
 * @param T Тип данных запроса.
 * */
typealias RequestErrorHandler<T> = (error: Throwable?, data: T?, loading: Loading?) -> Boolean

/**
 * **Реактор на изменение состояния жизненного цикла запроса.**
 *
 * Используется для реагирования на изменения состояния запроса, такие как:
 * `Загрузка`, `Ошибка` и `Успех`.
 *
 * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
 *
 * **Получает в качестве аргументов**: запрос, ui-данные и состояние загрузки.
 *
 * @param T Тип данных запроса.
 * @param D Тип ui-данных.
 * */
typealias RequestReactor<T, D> = (request: Request<T>, data: D?, loading: Loading?) -> Unit

/**
 * **Реактор на успешно завершившийся запрос.**
 *
 * Является урезанной версией [RequestReactor], для более лаконичного использования лямбд
 * (один аргумент против трех).
 *
 * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
 *
 * **Получает в качестве аргументов**: ui-данные.
 *
 * @param D Тип ui-данных.
 * */
typealias RequestDataReactor<D> = (data: D?) -> Unit

/**
 * **Реактор на неудачно завершившийся запрос.**
 *
 * Является урезанной версией [RequestReactor], для более лаконичного использования лямбд
 * (один аргумент против трех).
 *
 * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
 *
 * **Получает в качестве аргументов**: ошибку.
 * */
typealias RequestErrorReactor = (error: Throwable?) -> Unit

/**
 * **Реактор на загрузку запроса.**
 *
 * Является урезанной версией [RequestReactor], для более лаконичного использования лямбд
 * (один аргумент против трех).
 *
 * Может быть применен для отображения сообщений на UI, изменения полей состояния и т.д.
 *
 * **Получает в качестве аргументов**: состояние загрузки.
 * */
typealias RequestLoadingReactor = (loading: Loading?) -> Unit