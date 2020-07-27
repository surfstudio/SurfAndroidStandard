/*
  Copyright (c) 2020, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
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
