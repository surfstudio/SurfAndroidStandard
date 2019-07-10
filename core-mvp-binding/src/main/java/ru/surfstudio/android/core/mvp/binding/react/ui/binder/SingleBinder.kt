package ru.surfstudio.android.core.mvp.binding.react.ui.binder

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.StateHolder
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * [BaseRxBinder] со связью всех сущностей экрана "один к одному".
 */
class SingleBinder<T : Event, SH : StateHolder>(
        hub: RxEventHub<T>,
        middleware: RxMiddleware<T>,
        holder: SH,
        reactor: Reactor<T, SH>,
        basePresenterDependency: BasePresenterDependency
) : BaseRxBinder(basePresenterDependency) {

    init {
        bind(hub, middleware, holder, reactor)
    }
}