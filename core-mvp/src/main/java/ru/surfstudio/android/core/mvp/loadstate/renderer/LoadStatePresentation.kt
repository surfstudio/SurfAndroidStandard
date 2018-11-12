package ru.surfstudio.android.core.mvp.loadstate.renderer

import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

abstract class LoadStatePresentation<in T : LoadStateInterface> {

    val thisStateActions: MutableList<() -> Unit> = mutableListOf()
    val afterThisStateActions: MutableList<() -> Unit> = mutableListOf()

    abstract fun showLoadStatePresentation(loadStateFrom: LoadStateInterface, loadStateTo: T)

    abstract fun hideLoadStatePresentation(loadStateFrom: T, loadStateTo: LoadStateInterface)
}