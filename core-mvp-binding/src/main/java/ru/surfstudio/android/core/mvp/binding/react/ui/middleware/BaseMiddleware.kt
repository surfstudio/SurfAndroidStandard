package ru.surfstudio.android.core.mvp.binding.react.ui.middleware

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.react.builders.RxBuilderIO
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонный базовый [Middleware], который необходимо реализовать на проектах.
 */
abstract class BaseMiddleware<T : Event>(
        override val schedulersProvider: SchedulersProvider,
        override val errorHandler: ErrorHandler
) : RxMiddleware<T>, RxBuilderIO, RxBuilderHandleError