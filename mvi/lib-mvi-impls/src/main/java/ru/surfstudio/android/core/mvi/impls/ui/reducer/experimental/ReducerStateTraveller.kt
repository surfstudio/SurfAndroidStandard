package ru.surfstudio.android.core.mvi.impls.ui.reducer.experimental

import ru.surfstudio.android.core.mvi.impls.BuildConfig
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.logger.Logger

/**
 * Experimental mechanism for travelling between states in `MVI-Reducer` approach.
 */
@Deprecated("Currently not production ready solution")
object ReducerStateTraveller : StateEmitter {

    private val stateMap: HashMap<String, MutableList<TravelEntry<*>>?> = hashMapOf()

    private val isEnabled: Boolean = BuildConfig.DEBUG

    /**
     * Add [holder] and it's [state] to the storage.
     */
    fun <S : Any> accept(state: S, holder: State<S>) {
        if (!isEnabled) return
        val holderName = extractHolderName(holder)
        val list = stateMap[holderName] ?: mutableListOf()
        list.add(TravelEntry(state, holder))
        stateMap[holderName] = list
    }

    /**
     * Clear storage by given [holderName].
     */
    fun erase(holderName: String) {
        if (!isEnabled) return
        stateMap[holderName] = null
    }

    /**
     * Clear storage by given [holder].
     */
    fun erase(holder: State<*>) {
        if (!isEnabled) return
        erase(extractHolderName(holder))
    }

    /**
     * Travelling between states.
     *
     * @param holderName is target, that travelling between states.
     * @param index of state, we're travelling in.
     */
    fun travel(holderName: String, index: Int) {
        if (!isEnabled) return
        val list = stateMap[holderName] ?: return
        val entry = list.getOrNull(index) ?: return
        val holder = entry.holder as State<Any>
        holder.accept(entry.state)
    }

    /**
     * Print all of the states in storage by given [holderName].
     */
    fun print(holderName: String) {
        if (!isEnabled) return
        val list = stateMap[holderName] ?: return
        list.forEachIndexed { index, entry -> print(index, entry.state) }
    }

    private fun print(index: Int, state: Any) {
        if (!isEnabled) return
        Logger.d("State #$index: $state")
    }

    private fun extractHolderName(holder: State<*>) = holder::class.java.simpleName

    private data class TravelEntry<S : Any>(val state: S, val holder: State<S>)
}
