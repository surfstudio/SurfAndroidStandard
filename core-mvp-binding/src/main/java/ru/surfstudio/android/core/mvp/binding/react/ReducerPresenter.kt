package ru.surfstudio.android.core.mvp.binding.react

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

abstract class ReducerPresenter(
        basePresenterDependency: BasePresenterDependency
) : BaseRxPresenter(basePresenterDependency), EventHub, Reducer {

    abstract val reducers: List<Reducer>

    override fun <T : Event> acceptEvent(eventObservable: Observable<T>) {
        subscribe(eventObservable,
                {
                    acceptEvent(it)
                },
                {
                })
    }

    override fun <T : Event> react(event: T) {
        //Не определено, переопределить в случае необходимости
    }

    override fun <T : Event> acceptEvent(event: T) {
        react(event)
        reducers.forEach { it.react(event) }
    }

    fun <T, E : NetworkRequestEvent<T>> Observable<T>.sendEvent(event: E): Disposable {
        acceptEvent(event.acceptLoading())
        return subscribe(this,
                { acceptEvent(event.acceptData(it)) },
                { acceptEvent(event.acceptError(it)) }
        )
    }
}