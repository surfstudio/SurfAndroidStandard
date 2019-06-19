package ru.surfstudio.android.core.mvp.binding.react.loadable.event

import ru.surfstudio.android.core.mvp.binding.react.loadable.data.EmptyErrorException

/**
 * Тип загрузки данных уровня Interactor.
 *
 * Отражает цикл асинхронной загрузки данных:
 * 1. Получение данных начинается с состояния загрузки [LoadType.Loading],
 * 2. После этого либо приходят данные [LoadType.Data],
 * 3. Либо приходит ошибка загрузки данных [LoadType.Error]
 */
sealed class LoadType<T> {
    class Loading<T> : LoadType<T>()
    data class Data<T>(val data: T) : LoadType<T>()
    data class Error<T>(val error: Throwable = EmptyErrorException()) : LoadType<T>()
}
