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
package ru.surfstudio.android.core.mvi.ui.reducer

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * [Reducer] in terms of `Redux`:
 *
 * This entity is responsible to react on given [Event]
 * and produce new screen [State] as result of reaction.
 *
 * [Reducers documentation](https://redux.js.org/basics/reducers)
 */
interface Reducer<E : Event, S> : Reactor<E, State<S>> {

    override fun react(sh: State<S>, event: E) {
        val oldState = sh.value
        val newState = reduce(oldState, event)
        if (isStateChanged(oldState, newState)) {
            sh.accept(newState)
        }
    }

    /**
     * Used to check: if screen [State] has changed or not, after reaction on some [Event].
     *
     * So we can't skip screen rendering if [State] hasn't changed.
     *
     * Can be overridden if required some special checks or custom behaviour.
     */
    fun isStateChanged(oldState: S, newState: S) = oldState != newState

    /**
     * Transformation of current screen [State] by given [Event].
     *
     * @return updated screen [State].
     */
    fun reduce(state: S, event: E): S
}