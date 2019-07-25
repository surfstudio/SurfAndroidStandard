package ru.surfstudio.android.core.mvp.binding.rx.loadable.state

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.rx.loadable.data.LoadableData
import ru.surfstudio.android.core.mvp.binding.rx.loadable.data.Loading
import ru.surfstudio.android.core.mvp.binding.rx.extensions.filterValue
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.StateTarget
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW

/**
 * UI-State запроса на загрузку данных.
 * Содержит в себе неизменяемый экземпляр [LoadableData], который отражает текущее значение загрузки данных.
 *
 * Перед тем, как помещать LoadType в LoadableState, необходимо трансформировать его в LoadableData,
 * то есть, произвести трансформацию i-слой -> ui-слой.
 */
open class LoadableState<T> : BehaviorRelation<LoadableData<T>, VIEW, StateTarget>(LoadableData()) {

    override fun getConsumer(source: VIEW) = relay

    override fun getObservable(target: StateTarget) = relay.share()

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
        get() = relay.value!!.data.get()

    val dataOrNull: T?
        get() = relay.value!!.data.getOrNull()

    val load: Loading
        get() = relay.value!!.load

    val isLoading: Boolean
        get() = relay.value!!.load.isLoading

    val error: Throwable
        get() = relay.value!!.error

    fun modify(modifier: LoadableData<T>.() -> LoadableData<T>) {
        val value = relay.value ?: return
        relay.accept(modifier(value))
    }
}