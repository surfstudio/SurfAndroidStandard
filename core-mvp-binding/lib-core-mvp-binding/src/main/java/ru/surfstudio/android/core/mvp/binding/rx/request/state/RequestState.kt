package ru.surfstudio.android.core.mvp.binding.rx.request.state

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.StateTarget
import ru.surfstudio.android.core.mvp.binding.rx.request.data.Loading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi

/**
 * UI-State запроса на загрузку данных.
 * Содержит в себе неизменяемый экземпляр [RequestUi], который отражает текущее значение загрузки данных.
 *
 * Перед тем, как помещать Request в RequestState, необходимо трансформировать его в RequestUi,
 * то есть, произвести трансформацию i-слой -> ui-слой.
 */
open class RequestState<T>(
        initialResponse: RequestUi<T>
) : BehaviorRelation<RequestUi<T>, PRESENTER, StateTarget>(initialResponse) {

    constructor(initialData: T) : this(RequestUi(data = initialData))

    constructor(initialLoading: Loading) : this(RequestUi(load = initialLoading))

    constructor() : this(RequestUi())

    override fun getConsumer(source: PRESENTER) = relay

    override fun getObservable(target: StateTarget) = relay.share()

    fun observeData(): Observable<T> = relay.share()
            .flatMap { skipIfNull(it.data) }

    fun observeOptionalData(): Observable<Optional<T>> = relay.share()
            .map { if (it.data == null) Optional.empty() else Optional.of(it.data) }
            .distinctUntilChanged()

    fun observeLoading(): Observable<Loading> = relay.share()
            .flatMap { skipIfNull(it.load) }
            .distinctUntilChanged()

    fun observeIsLoading(): Observable<Boolean> = observeLoading()
            .map { it.isLoading }

    fun observeError(): Observable<Throwable> = relay.share()
            .flatMap { skipIfNull(it.error) }

    fun observeOptionalError(): Observable<Optional<Throwable>> = relay.share()
            .map { if (it.error == null) Optional.empty() else Optional.of(it.error) }
            .distinctUntilChanged()

    fun observeHasError(): Observable<Boolean> = relay.share()
            .map { it.error != null }
            .distinctUntilChanged()

    val data: T?
        get() = relay.value!!.data

    val loading: Loading?
        get() = relay.value!!.load

    val isLoading: Boolean
        get() = relay.value!!.load?.isLoading ?: false

    val error: Throwable?
        get() = relay.value!!.error

    fun modify(modifier: RequestUi<T>.() -> RequestUi<T>) {
        val value = relay.value ?: return
        relay.accept(modifier(value))
    }

    private fun <T> skipIfNull(value: T?): Observable<T> {
        return if (value == null) Observable.empty() else Observable.just(value)
    }
}