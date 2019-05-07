package ru.surfstudio.android.core.mvp.binding.rx.relation.mvp

/**
 * [State], который поддерживает состояние загрузки
 *
 * @property isLoading      флаг, отвечающий за отображение состояния загрузки блока данных
 *                          true, пока данные загружаются), false при ошибке или успехе.
 *
 * @property error          ошибка, которая может возникнуть при загрузке данных.
 *                          Если ошибки нет, эмитится EmptyErrorException
 */
class LoadableState<T> : State<T>() {
    val isLoading = State<Boolean>()
    val error = State<Throwable>(EmptyErrorException())
}