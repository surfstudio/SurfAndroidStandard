package ru.surfstudio.android.core.mvp.binding.react.ui.binder

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.StateHolder
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Класс, который связывает все сущности скопа экрана в одну и производит подписку
 *
 * TODO убрать зависимость от presenter
 */
abstract class BaseRxBinder(basePresenterDependency: BasePresenterDependency) : BaseRxPresenter(basePresenterDependency) {

    fun <T : Event, SH : StateHolder> bind(
            eventHub: RxEventHub<T>,
            middleware: RxMiddleware<T>,
            stateHolder: SH,
            reactor: Reactor<T, SH>
    ) {
        middleware.transform(eventHub.observeEvents()) as Observable<T> bindTo eventHub
        eventHub.observeEvents().bindTo(stateHolder, reactor)
    }

    fun <T : Event> bind(
            eventHub: RxEventHub<T>,
            middleware: RxMiddleware<T>
    ) {
        (middleware.transform(eventHub.observeEvents()) as Observable<T>).bindIgnore()
    }

    fun <T : Event, SH : StateHolder> Observable<T>.bindTo(stateHolder: SH, reactor: Reactor<T, SH>) =
            subscribe(
                    this,
                    { reactor.react(stateHolder, it) },
                    ::logError
            )

    private fun <T> Observable<T>.bindIgnore() {
        subscribe(this,
                {
                    //ignore
                },
                ::logError)
    }

    private fun logError(throwable: Throwable) {
        //TODO сделать логгирование ошибок, пока в модуле не подключен Logger
    }
}