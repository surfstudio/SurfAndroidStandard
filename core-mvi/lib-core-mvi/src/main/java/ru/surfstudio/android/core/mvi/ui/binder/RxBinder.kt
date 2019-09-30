package ru.surfstudio.android.core.mvi.ui.binder

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvi.ui.binder.error.EventHubChainException
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor

/**
 * Класс, который связывает все сущности скопа экрана в одну и производит подписку
 */
interface RxBinder {

    fun <T : Event, SH> bind(
            eventHub: RxEventHub<T>,
            middleware: RxMiddleware<T>,
            stateHolder: SH,
            reactor: Reactor<T, SH>
    ) {
        val eventHubObservable = eventHub
                .observe()
                .doOnNext { event -> reactor.react(stateHolder, event) }
                .share()    //Создаем новый observable, чтобы избежать двойного подхватывания значений

        middleware.transform(eventHubObservable) as Observable<T> bindEvents eventHub
    }

    fun <T : Event, SH> bind(
            eventHub: RxEventHub<T>,
            middlewares: List<RxMiddleware<T>>,
            stateHolder: SH,
            reactor: Reactor<T, SH>
    ) {
        val eventHubObservable = eventHub
                .observe()
                .doOnNext { event -> reactor.react(stateHolder, event) }
                .share()    //Создаем новый observable, чтобы избежать двойного подхватывания значений

        middlewares.forEach { it.transform(eventHubObservable) as Observable<T> bindEvents eventHub }
    }

    fun <T : Event> bind(
            eventHub: RxEventHub<T>,
            middleware: RxMiddleware<T>
    ) {
        val eventHubObservable = eventHub.observe().share()
        middleware.transform(eventHubObservable) as Observable<T> bindEvents eventHub
    }

    fun <T : Event, SH> bind(
            eventHub: RxEventHub<T>,
            stateHolder: SH,
            reactor: Reactor<T, SH>
    ) {
        eventHub.observe().bindUi(stateHolder, reactor)
    }

    infix fun <T> Observable<T>.bindEvents(consumer: Consumer<T>) =
            subscribe(this, consumer::accept, ::onError)

    fun <T : Event, SH> Observable<T>.bindUi(stateHolder: SH, reactor: Reactor<T, SH>) =
            subscribe(this, { reactor.react(stateHolder, it) }, ::onError)

    fun <T> subscribe(
            observable: Observable<T>,
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit
    ): Disposable

    /**
     * Поведение при получении ошибки в подписке на [EventHub]
     *
     * Переопределив этот метод можно логгировать, выбрасывать, или игнорировать ошибки.
     *
     * @param throwable ошибка, возникшая в цепочке событий.
     */
    fun onError(throwable: Throwable) {
        throw EventHubChainException(throwable)
    }
}