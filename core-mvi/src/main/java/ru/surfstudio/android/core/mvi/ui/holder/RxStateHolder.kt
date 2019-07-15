package ru.surfstudio.android.core.mvi.ui.holder

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.effect.SideEffect
import ru.surfstudio.android.core.mvi.ui.effect.ObservableSideEffect
import ru.surfstudio.android.core.mvi.ui.effect.RelationSideEffect
import ru.surfstudio.android.core.mvp.binding.rx.relation.Relation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.StateTarget

/**
 * Класс, содержащий состояние экрана
 *
 * @property sideEffects список возможных сайд-эффектов для состояний экрана
 */
interface RxStateHolder<E : Event> {

    val sideEffects: List<SideEffect<out E, *>>

    infix fun <T> Observable<T>.with(transformer: (T) -> E): SideEffect<E, T> =
            ObservableSideEffect(this, transformer)

    infix fun <T> Relation<T, PRESENTER, StateTarget>.with(transformer: (T) -> E): SideEffect<E, T> =
            RelationSideEffect(this, transformer)
}