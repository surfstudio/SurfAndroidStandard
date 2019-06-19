package ru.surfstudio.android.core.mvp.binding.react.loadable

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.*
import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.react.optional.filterValue
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW
import java.lang.NullPointerException

/**
 * UI-State запроса на загрузку данных.
 * Содержит в себе неизменяемый экземпляр [LoadableData], который отражает текущее значение загрузки данных.
 */
open class LoadableState<T> : BehaviorRelation<LoadableData<T>, PRESENTER, VIEW>(LoadableData()) {

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

    val observeError: Observable<Throwable>
        get() = relay.share()
                .map { it.error }

    val data: T
        get() = relay.value!!.data.get() ?: throw NullPointerException()

    val dataOrNull: T?
        get() = relay.value!!.data.getOrNull()

    val isLoading: Boolean
        get() = relay.value!!.load.isLoading

    val error: Throwable
        get() = relay.value!!.error

    override fun getConsumer(source: PRESENTER): Consumer<LoadableData<T>> = relay

    override fun getObservable(target: VIEW): Observable<LoadableData<T>> = relay.share()

    fun acceptEvent(event: LoadableEvent<T>) {
        relay.accept(event.toLoadableData())
    }

    fun modify(modifier: LoadableData<T>.() -> LoadableData<T>) {
        val value = relay.value ?: return
        relay.accept(modifier(value))
    }
}