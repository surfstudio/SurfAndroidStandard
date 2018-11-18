package ru.surfstudio.android.core.mvp.loadstate.renderer

import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

/**
 * Интерфейс, который обязано реализовывать любое представление лоадстейта
 */
interface LoadStatePresentation<in T : LoadStateInterface> {

    fun showPresentation(loadStateFrom: LoadStateInterface, loadStateTo: T)

    fun hidePresentation(loadStateFrom: T, loadStateTo: LoadStateInterface)
}