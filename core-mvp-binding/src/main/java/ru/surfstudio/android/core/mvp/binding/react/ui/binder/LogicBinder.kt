package ru.surfstudio.android.core.mvp.binding.react.ui.binder

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.Middleware
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Простейший биндер, в котором отображение логики на UI не нужно.
 */
class LogicBinder<T : Event>(
        hub: RxEventHub<T>,
        middleware: Middleware<T>,
        basePresenterDependency: BasePresenterDependency
) : Binder(basePresenterDependency) {

    init {
        bind(hub, middleware)
    }
}