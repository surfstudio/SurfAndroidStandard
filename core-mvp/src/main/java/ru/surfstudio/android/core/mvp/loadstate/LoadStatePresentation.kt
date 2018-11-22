package ru.surfstudio.android.core.mvp.loadstate

/**
 * Интерфейс, который обязано реализовывать любое представление лоадстейта
 */
interface LoadStatePresentation<in T : LoadStateInterface> {

    fun showState(state: T, previousState: LoadStateInterface)

    fun hideState(state: T, nextState: LoadStateInterface)
}