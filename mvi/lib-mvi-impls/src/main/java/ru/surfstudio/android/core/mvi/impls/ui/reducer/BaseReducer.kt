package ru.surfstudio.android.core.mvi.impls.ui.reducer

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactor
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.ui.reducer.Reducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.ui.state.LifecycleStage

/**
 * Base implementation of [Reducer].
 *
 * Contains many useful methods to handle requests easily with error handling (inherited from [BaseReactor]).
 * */
abstract class BaseReducer<E : Event, S : Any>(
        baseReactorDependency: BaseReactorDependency,
        baseReducerDependency: BaseReducerDependency
) : BaseReactor<E, State<S>>(baseReactorDependency), Reducer<E, S> {

    protected val stateTraveller = baseReducerDependency.stateTraveller

    override fun react(sh: State<S>, event: E) {
        val oldState = sh.value
        super.react(sh, event)
        val newState = sh.value
        if (isStateChanged(oldState, newState)) {
            stateTraveller.accept(newState, sh)
        }
    }

    private fun eraseTravellerOnDestroy(sh: State<S>, event: E) {
        if (event is LifecycleEvent && event.stage == LifecycleStage.VIEW_DESTROYED) {
            stateTraveller.erase(sh)
        }
    }

}