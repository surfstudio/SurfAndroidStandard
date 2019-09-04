package ru.surfstudio.android.core.mvp.binding.rx.loadable.data

import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional

/**
 * Данные для отображения на Ui асинхронного запроса на получение данных
 *
 * @param data  данные, либо их отсутствие
 *              (используется [Optional] для предотвращения проблем с nullable-переменными)
 * @param load  обертка над состоянием загрузки, интерпретируемая на ui
 * @param error состояние ошибки
 */
data class LoadableData<T>(
        val data: Optional<T> = Optional.empty(),
        val load: Loading = MainLoading(false),
        val error: Throwable = EmptyErrorException()
)