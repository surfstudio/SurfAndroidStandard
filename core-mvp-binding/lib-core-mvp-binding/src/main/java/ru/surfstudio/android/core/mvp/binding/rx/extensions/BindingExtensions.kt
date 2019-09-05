package ru.surfstudio.android.core.mvp.binding.rx.extensions

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.Relation
import ru.surfstudio.android.core.mvp.binding.rx.relation.RelationEntity
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * Добавляет `[State]<Boolean>` к [Completable] и присваивает значение true когда тот выполняется,
 * и `false` когда выполнение окончено (с успехоим или нет)
 */
fun Completable.addLoadingState(
        presenter: Related<PRESENTER>,
        vararg loadingState: State<Boolean>
): Completable = this.run {
    presenter.run {
        doOnSubscribe { loadingState.forEach { it.accept(true) } }
                .doFinally { loadingState.forEach { it.accept(false) } }
    }
}

/**
 * Добавляет `[State]<Boolean>` к [Single]и присваивает значение true когда тот выполняется,
 * и `false` когда выполнение окончено (с успехоим или нет)
 */
fun <T> Single<T>.addLoadingState(
        presenter: Related<PRESENTER>,
        vararg loadingState: State<Boolean>
): Single<T> = this.run {
    presenter.run {
        doOnSubscribe { loadingState.forEach { it.accept(true) } }
                .doFinally { loadingState.forEach { it.accept(false) } }
    }
}

/**
 * Добавляет `[State]<Boolean>` к [Maybe] и присваивает значение true когда тот выполняется,
 * и `false` когда выполнение окончено (с успехоим или нет)
 */
fun <T> Maybe<T>.addLoadingState(
        presenter: Related<PRESENTER>,
        vararg loadingState: State<Boolean>
): Maybe<T> = this.run {
    presenter.run {
        doOnSubscribe { loadingState.forEach { it.accept(true) } }
                .doFinally { loadingState.forEach { it.accept(false) } }
    }
}

/**
 * Добавляет `[State]<Boolean>` к [Observable] и присваивает значение true когда тот выполняется,
 * и `false` когда выполнение окончено (с успехоим или нет)
 */
fun <T> Observable<T>.addLoadingState(
        presenter: Related<PRESENTER>,
        vararg loadingState: State<Boolean>
): Observable<T> = this.run {
    presenter.run {
        doOnSubscribe { loadingState.forEach { it.accept(true) } }
                .doFinally { loadingState.forEach { it.accept(false) } }
    }
}

/**
 * Оборачивает текущий [Relation] в observable обертывая в [Optional] и посылая первое значение пустое.
 * Обеспечивает гарантированный эмит значения от [Relation].
 *
 * @param relationEntity сторона получатель значений, перезентер или вью.
 * Как правило, передается `this`
 */
fun <V, T : RelationEntity> Relation<V, *, T>.toOptionalObservable(
        relationEntity: Related<T>
): Observable<Optional<V>> = this.run {
    relationEntity.run {
        observable.map { Optional.of(it) }
                .startWith(Optional.empty())
    }
}