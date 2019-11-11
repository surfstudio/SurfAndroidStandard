package ru.surfstudio.android.core.mvi.impls.ui.middleware

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.FlatMapMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * Шаблонный базовый [RxMiddleware], c простейшей реализацией функции transform:
 * вместо transform(observable<event>): observable<event>
 * используется flatMap(event): observable<event>,
 * с которым можно просто и понято работать в when-блоке.
 */
abstract class BaseMapMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMiddleware<T>(baseMiddlewareDependency), FlatMapMiddleware<T>