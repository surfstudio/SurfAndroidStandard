package ru.surfstudio.android.core.mvp.loadstate

/**
 * Упрощенная версия {@link LoadStatePresentation}
 */
abstract class SimpleLoadStatePresentation<in T : LoadStateInterface> : LoadStatePresentation<T> {

    abstract fun showState(state: T)

    override fun showState(state: T, previousState: LoadStateInterface) {
        showState(state)
    }

    override fun hideState(state: T, nextState: LoadStateInterface) {
        //ignore
    }
}