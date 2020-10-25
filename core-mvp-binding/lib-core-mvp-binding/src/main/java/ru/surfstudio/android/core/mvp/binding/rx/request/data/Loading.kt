package ru.surfstudio.android.core.mvp.binding.rx.request.data

/**
 * Обертка для отображения состояния загрузки на Ui.
 *
 * Можно реализовать у себя в проекте все необходимые состояния, унаследовавшись от этого класса.
 */
interface Loading {
    val isLoading: Boolean
}

/**
 * Состояние загрузки, используемое для кейсов, в которых важен сам факт осуществления загрузки.
 * */
data class SimpleLoading(override val isLoading: Boolean) : Loading

/**
 * Состояние загрузки, когда на экране нет контента
 */
class MainLoading(override val isLoading: Boolean) : Loading

/**
 * Состояние загрузки, когда на экране есть какой-то контент
 */
class TransparentLoading(override val isLoading: Boolean) : Loading

/**
 * Состояние загрузки при вызове через SwipeRefresh
 */
class SwipeRefreshLoading(override val isLoading: Boolean) : Loading