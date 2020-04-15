package ru.surfstudio.android.core.mvi.impls.ui.reducer

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactor
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.ui.reducer.Reducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * Base implementation of [Reducer].
 * */
abstract class BaseReducer<E : Event, S : Any>(
        baseReactorDependency: BaseReactorDependency
) : BaseReactor<E, State<S>>(baseReactorDependency), Reducer<E, S> {

    override fun react(sh: State<S>, event: E) {
        val oldState = sh.value
        val newState = reduce(oldState, event)
        if (isStateChanged(oldState, newState)) {
            sh.accept(newState)
        }
    }
}