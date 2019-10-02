package ru.surfstudio.android.core.mvp.binding.rx.request.state

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.rx.extensions.toOptional
import ru.surfstudio.android.core.mvp.binding.rx.request.data.ResponseUi
import ru.surfstudio.android.core.mvp.binding.rx.request.data.Loading
import ru.surfstudio.android.core.mvp.binding.rx.extensions.filterValue
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.StateTarget

/**
 * UI-State запроса на загрузку данных.
 * Содержит в себе неизменяемый экземпляр [ResponseUi], который отражает текущее значение загрузки данных.
 *
 * Перед тем, как помещать Request в RequestState, необходимо трансформировать его в ResponseUi,
 * то есть, произвести трансформацию i-слой -> ui-слой.
 */
open class RequestState<T>(
        initialResponse: ResponseUi<T>
) : BehaviorRelation<ResponseUi<T>, PRESENTER, StateTarget>(initialResponse) {

    constructor(initialData: T) : this(ResponseUi(data = initialData.toOptional()))

    constructor(initialLoading: Loading) : this(ResponseUi(load = initialLoading))

    constructor() : this(ResponseUi())

    override fun getConsumer(source: PRESENTER) = relay

    override fun getObservable(target: StateTarget) = relay.share()

    fun observeData(): Observable<T> = relay.share()
            .map { it.data }
            .filterValue()

    fun observeLoading(): Observable<Loading> = relay.share()
            .map { it.load }
            .distinctUntilChanged()

    fun observeIsLoading(): Observable<Boolean> = observeLoading().map { it.isLoading }

    fun observeError(): Observable<Throwable> = relay.share()
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

    fun modify(modifier: ResponseUi<T>.() -> ResponseUi<T>) {
        val value = relay.value ?: return
        relay.accept(modifier(value))
    }
}