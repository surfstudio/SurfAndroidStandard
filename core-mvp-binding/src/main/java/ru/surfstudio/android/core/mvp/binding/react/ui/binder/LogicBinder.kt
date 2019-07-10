package ru.surfstudio.android.core.mvp.binding.react.ui.binder

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Простейший биндер, в котором отображение логики на UI не нужно.
 */
class LogicBinder<T : Event>(
        hub: RxEventHub<T>,
        middleware: RxMiddleware<T>,
        basePresenterDependency: BasePresenterDependency
) : BaseRxBinder(basePresenterDependency) {

    init {
        bind(hub, middleware)
    }
}