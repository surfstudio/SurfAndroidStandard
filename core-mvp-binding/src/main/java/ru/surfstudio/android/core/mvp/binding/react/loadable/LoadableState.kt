package ru.surfstudio.android.core.mvp.binding.react.loadable

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional
import ru.surfstudio.android.core.mvp.binding.react.optional.filterValue
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW

class LoadableState<T>(
        initialValue: LoadableData<T> = LoadableData()
) : BehaviorRelation<LoadableData<T>, PRESENTER, VIEW>(initialValue) {

    val observeData: Observable<T>
        get() = relay.share()
                .map { it.data }
                .filterValue()

    val observeLoading: Observable<Boolean>
        get() = relay.share()
                .map { it.isLoading }
                .distinctUntilChanged()

    val observeError: Observable<Optional<Throwable>>
        get() = relay.share()
                .map { it.error }

    override fun getConsumer(source: PRESENTER): Consumer<LoadableData<T>> = relay

    override fun getObservable(target: VIEW): Observable<LoadableData<T>> = relay.share()

    fun acceptEvent(event: LoadableEvent<T>) {
        relay.accept(event.value)
    }

    fun modify(modifier: LoadableData<T>.() -> LoadableData<T>) {
        val value = relay.value ?: return
        relay.accept(modifier(value))
    }
}