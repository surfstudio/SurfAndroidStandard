package ru.surfstudio.android.core.mvp.binding.react.ui

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.RxEvent
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.middleware.MiddleWareHolder
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

abstract class BaseMiddleWare(
        basePresenterDependency: BasePresenterDependency,
        private val hub: RxEventHub
) : BaseRxPresenter(basePresenterDependency), MiddleWareHolder {


    @CallSuper
    override fun onLoad(viewRecreated: Boolean) {
        subscribe(hub.observeEvents(), ::accept, ::logError)
    }

    private fun logError(throwable: Throwable) {
        //TODO
    }

    fun <T, E : RxEvent<T>> Observable<T>.sendEvent(event: E): Disposable {
        hub.accept(event.acceptLoading())
        return subscribe(this,
                { hub.accept(event.acceptData(it)) },
                { hub.accept(event.acceptError(it)) }
        )
    }

    fun sendEvent(event: Event) {
        hub.accept(event)
    }
}