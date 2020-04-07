package ru.surfstudio.android.core.mvp.binding.rx.request.data

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
 * @param load  обертка над состоянием загрузки, интерпретируемая на ui
 * @param error состояние ошибки
 */
data class RequestUi<T>(
        val data: T? = null,
        val load: Loading? = null,
        val error: Throwable? = null
) {

    /** Запрос загружается? */
    val isLoading: Boolean get() = load?.isLoading ?: false

    /** Есть данные? */
    val hasData: Boolean get() = data != null

    /** Есть ошибка? */
    val hasError: Boolean get() = error != null

}