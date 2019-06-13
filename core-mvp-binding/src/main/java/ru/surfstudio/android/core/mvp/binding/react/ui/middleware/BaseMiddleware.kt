package ru.surfstudio.android.core.mvp.binding.react.ui.middleware

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableEvent
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter

abstract class BaseMiddleware(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseRxPresenter(baseMiddlewareDependency.basePresenterDependency), Middleware {

    val hub = baseMiddlewareDependency.hub

    @CallSuper
    override fun onFirstLoad() {
        subscribeToHub()
    }

    @CallSuper
    override fun onLoad(viewRecreated: Boolean) {
        if (viewRecreated) subscribeToHub()
    }

    private fun logError(throwable: Throwable) {
        //TODO сделать логгирование ошибок, пока в модуле не подключен Logger
    }

    fun <T, E : LoadableEvent<T>> Observable<T>.send(event: E): Disposable {
        hub.emitEvent(event.acceptLoading())
        return subscribe(this,
                { hub.emitEvent(event.acceptData(it)) },
                { hub.emitEvent(event.acceptError(it)) }
        )
    }

    private fun subscribeToHub() {
        subscribe(hub.observeEvents(), ::accept, ::logError)
    }
}