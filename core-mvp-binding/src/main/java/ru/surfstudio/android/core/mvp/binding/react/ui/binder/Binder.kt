package ru.surfstudio.android.core.mvp.binding.react.ui.binder

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.reactor.Reducer
import ru.surfstudio.android.core.mvp.binding.react.reactor.StateHolder
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.Middleware
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

abstract class Binder(basePresenterDependency: BasePresenterDependency) : BaseRxPresenter(basePresenterDependency) {

    fun <T : Event, SH : StateHolder> Observable<T>.bindTo(stateHolder: SH, reducer: Reducer<T, SH>) =
            subscribe(
                    this,
                    { reduce(reducer, stateHolder, it) },
                    ::logError
            )

    fun <T : Event, SH : StateHolder> bindBunch(
            eventHub: RxEventHub<T>,
            middleware: Middleware<T>,
            stateHolder: SH,
            reducer: Reducer<T, SH>
    ) {
        eventHub.observeEvents().bindTo(stateHolder, reducer)
        eventHub.observeEvents().flatMap(middleware::flatMap).bindTo(stateHolder, reducer)
    }

    private fun <T : Event, SH : StateHolder> reduce(
            reducer: Reducer<T, SH>,
            stateHolder: SH,
            event: T
    ) {
        reducer.reduce(stateHolder, event)
        //TODO сделать поглощение результата reduce при переходе на каноничный MVI
    }

    private fun logError(throwable: Throwable) {
        //TODO сделать логгирование ошибок, пока в модуле не подключен Logger
    }
}