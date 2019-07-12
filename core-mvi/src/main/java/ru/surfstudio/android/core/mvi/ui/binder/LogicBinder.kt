package ru.surfstudio.android.core.mvi.ui.binder

import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * Простейший биндер, в котором отображение логики на UI не нужно.
 */
class LogicBinder<T : Event>(
        hub: RxEventHub<T>,
        middleware: RxMiddleware<T>,
        basePresenterDependency: BasePresenterDependency
) : RxBinder(basePresenterDependency) {

    init {
        bind(hub, middleware)
    }
}