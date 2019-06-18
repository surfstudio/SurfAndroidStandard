package ru.surfstudio.android.core.mvp.binding.react.ui.middleware

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.rx_builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.react.rx_builders.RxBuilderIO
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

abstract class BaseMiddleware<T : Event>(
        override val schedulersProvider: SchedulersProvider,
        override val errorHandler: ErrorHandler
) : Middleware<T>, RxBuilderIO, RxBuilderHandleError