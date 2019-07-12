package ru.surfstudio.android.core.mvi.ui.binder

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvi.ui.reactor.StateHolder

/**
 * Класс, который связывает все сущности скопа экрана в одну и производит подписку
 *
 * TODO убрать зависимость от presenter
 */
open class RxBinder(basePresenterDependency: BasePresenterDependency) : BaseRxPresenter(basePresenterDependency) {

    fun <T : Event, SH : StateHolder> bind(
            eventHub: RxEventHub<T>,
            middleware: RxMiddleware<T>,
            stateHolder: SH,
            reactor: Reactor<T, SH>
    ) {
        middleware.transform(eventHub.observe()) as Observable<T> bindTo eventHub
        eventHub.observe().bindTo(stateHolder, reactor)
    }

    fun <T : Event> bind(
            eventHub: RxEventHub<T>,
            middleware: RxMiddleware<T>
    ) {
        (middleware.transform(eventHub.observe()) as Observable<T>).bindIgnore()
    }

    protected fun <T : Event, SH : StateHolder> Observable<T>.bindTo(stateHolder: SH, reactor: Reactor<T, SH>) =
            subscribe(
                    this,
                    { reactor.react(stateHolder, it) },
                    ::onError
            )

    protected fun <T> Observable<T>.bindIgnore() {
        subscribe(this,
                {
                    //ignore
                },
                ::onError)
    }

    /**
     * Поведение при получении ошибки в подписке на [EventHub]
     *
     * Переопределив этот метод можно логгировать, выбрасывать, или игнорировать ошибки.
     *
     * @param throwable ошибка, возникшая в цепочке событий.
     */
    protected fun onError(throwable: Throwable) {
        throw throwable
    }

}