package ru.surfstudio.android.core.mvp.binding.react.loadable

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.*
import ru.surfstudio.android.core.mvp.binding.react.optional.filterValue
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW

open class LoadableState<T>(
        initialValue: LoadableData<T> = LoadableData()
) : BehaviorRelation<LoadableData<T>, PRESENTER, VIEW>(initialValue) {

    val observeData: Observable<T>
        get() = relay.share()
                .map { it.data }
                .filterValue()

    val observeLoad: Observable<Loading>
        get() = relay.share()
                .map { it.load }
                .distinctUntilChanged()

    val observeLoading: Observable<Boolean>
        get() = observeLoad.map { it.isLoading }

    val observeError: Observable<Error>
        get() = relay.share()
                .map { it.error }

    override fun getConsumer(source: PRESENTER): Consumer<LoadableData<T>> = relay

    override fun getObservable(target: VIEW): Observable<LoadableData<T>> = relay.share()

    fun acceptEvent(event: LoadableEvent<T>) {
        relay.accept(LoadableData(event.data, MainLoading(event.isLoading), CommonError(event.error)))
    }

    fun modify(modifier: LoadableData<T>.() -> LoadableData<T>) {
        val value = relay.value ?: return
        relay.accept(modifier(value))
    }
}