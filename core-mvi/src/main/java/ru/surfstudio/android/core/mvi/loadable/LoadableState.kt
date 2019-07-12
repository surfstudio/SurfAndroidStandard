package ru.surfstudio.android.core.mvi.loadable

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.loadable.event.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvi.loadable.data.LoadableData
import ru.surfstudio.android.core.mvi.loadable.data.Loading
import ru.surfstudio.android.core.mvi.optional.filterValue
import java.lang.NullPointerException

/**
 * UI-State запроса на загрузку данных.
 * Содержит в себе неизменяемый экземпляр [LoadableData], который отражает текущее значение загрузки данных.
 */
open class LoadableState<T> : State<LoadableData<T>>(LoadableData()) {

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

    fun acceptEvent(event: LoadableEvent<T>) {
        relay.accept(event.toLoadableData())
    }

    fun modify(modifier: LoadableData<T>.() -> LoadableData<T>) {
        val value = relay.value ?: return
        relay.accept(modifier(value))
    }
}