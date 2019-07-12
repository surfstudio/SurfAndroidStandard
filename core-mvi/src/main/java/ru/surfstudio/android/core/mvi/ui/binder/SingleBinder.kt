package ru.surfstudio.android.core.mvi.ui.binder

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvi.ui.reactor.StateHolder

/**
 * [RxBinder] со связью всех сущностей экрана "один к одному".
 */
class SingleBinder<T : Event, SH : StateHolder>(
        hub: RxEventHub<T>,
        middleware: RxMiddleware<T>,
        holder: SH,
        reactor: Reactor<T, SH>,
        basePresenterDependency: BasePresenterDependency
) : RxBinder(basePresenterDependency) {

    init {
        bind(hub, middleware, holder, reactor)
    }
}