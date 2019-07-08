package ru.surfstudio.android.core.mvp.binding.react.ui.binder

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.StateHolder
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.Middleware
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * [Binder] со связью всех сущностей экрана "один к одному".
 */
class SingleBinder<T : Event, SH : StateHolder>(
        hub: RxEventHub<T>,
        middleware: Middleware<T>,
        holder: SH,
        reactor: Reactor<T, SH>,
        basePresenterDependency: BasePresenterDependency
) : Binder(basePresenterDependency) {

    init {
        bind(hub, middleware, holder, reactor)
    }
}