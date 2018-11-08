package ru.surfstudio.android.core.mvp.loadstate.renderer

import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

interface LoadStatePresentationStrategy<in T : LoadStateInterface> {
    fun renderLoadState(state: T)
}