package ru.surfstudio.android.core.mvi.ui

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * View, содержащий один state экрана, и умеющий его отрисовывать
 */
interface SingleStateView<S> {

    /**
     * Хранитель актульного состояния экрана
     */
    val sh: State<S>

    /**
     * Отрисовка состояния экрана
     */
    fun render(state: S)

    /**
     * Инициализация вью, установка лиснеров
     */
    fun initViews()
}
