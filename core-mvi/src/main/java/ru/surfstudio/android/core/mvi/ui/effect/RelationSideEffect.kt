package ru.surfstudio.android.core.mvi.ui.effect

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.relation.StateObserver
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation
import ru.surfstudio.android.core.mvp.binding.rx.relation.Relation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.StateTarget
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW

/**
 * [SideEffect] для [Relation]
 */
class RelationSideEffect<E : Event, T>(
        private val relation: Relation<T, PRESENTER, StateTarget>,
        override val eventTransformer: (T) -> E
) : SideEffect<E, T>, StateObserver {
    override val observable: Observable<T>
        get() = relation.observable
}