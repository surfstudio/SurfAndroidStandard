package ru.surfstudio.android.core.mvp.binding.react.ui.binder

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.reactor.Reducer
import ru.surfstudio.android.core.mvp.binding.react.reactor.StateHolder
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.Middleware
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Класс, который связывает все сущности скопа экрана в одну и производит подписку
 */
abstract class Binder(basePresenterDependency: BasePresenterDependency) : BaseRxPresenter(basePresenterDependency) {

    fun <T : Event, SH : StateHolder> bind(
            eventHub: RxEventHub<T>,
            middleware: Middleware<T>,
            stateHolder: SH,
            reducer: Reducer<T, SH>
    ) {
        middleware.transform(eventHub.observeEvents()) as Observable<T> bindTo eventHub
        eventHub.observeEvents().bindTo(stateHolder, reducer)
    }

    fun <T : Event> bind(
            eventHub: RxEventHub<T>,
            middleware: Middleware<T>
    ) {
        (middleware.transform(eventHub.observeEvents()) as Observable<T>).bindIgnore()
    }

    fun <T : Event, SH : StateHolder> Observable<T>.bindTo(stateHolder: SH, reducer: Reducer<T, SH>) =
            subscribe(
                    this,
                    { reduce(reducer, stateHolder, it) },
                    ::logError
            )

    private fun <T> Observable<T>.bindIgnore() {
        subscribe(this,
                {
                    //ignore
                },
                ::logError)
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