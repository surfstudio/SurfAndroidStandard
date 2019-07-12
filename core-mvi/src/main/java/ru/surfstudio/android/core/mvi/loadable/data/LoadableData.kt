package ru.surfstudio.android.core.mvi.loadable.data

import ru.surfstudio.android.core.mvi.optional.Optional

/**
 * Данные для отображения на Ui асинхронного запроса на получение данных
 *
 * @param data  данные, либо их отсутствие
 *              (используется [Optional] для предотвращения проблем с nullable-переменными)
 * @param load  обертка над состоянием загрузки, интерпретируемая на ui
 * @param error состояние ошибки
 */
data class LoadableData<T>(
        val data: Optional<T> = Optional.Empty,
        val load: Loading = MainLoading(false),
        val error: Throwable = EmptyErrorException()
)