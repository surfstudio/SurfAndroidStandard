package ru.surfstudio.android.core.mvp.binding.rx.request.data

import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional

/**
 * Данные для отображения на Ui асинхронного запроса на получение данных.
 *
 * В отличие от Request, содержащего только состояние запроса в текущий момент времени
 * (либо загрузка, либо данные, либо ошибка),
 * этот класс содержит комбинацию из них
 * (загрузка + данные + ошибка),
 *
 * Кроме того, содержит информацию о том, как именно состояние загрузки данных
 * должно быть отображено на UI: [Loading]
 *
 * @param data  данные, либо их отсутствие
 *              (используется [Optional] для предотвращения проблем с nullable-переменными)
 * @param load  обертка над состоянием загрузки, интерпретируемая на ui
 * @param error состояние ошибки
 */
data class ResponseUi<T>(
        val data: Optional<T> = Optional.empty(),
        val load: Loading = MainLoading(false),
        val error: Throwable = EmptyErrorException()
)