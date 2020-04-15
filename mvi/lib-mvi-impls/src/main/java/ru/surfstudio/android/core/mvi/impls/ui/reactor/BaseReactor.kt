package ru.surfstudio.android.core.mvi.impls.ui.reactor

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor

/**
 * Base implementation of [Reactor].
 * */
abstract class BaseReactor<E : Event, SH>(
        baseReactorDependency: BaseReactorDependency
) : Reactor<E, SH> {

    protected val errorHandler = baseReactorDependency.errorHandler
}