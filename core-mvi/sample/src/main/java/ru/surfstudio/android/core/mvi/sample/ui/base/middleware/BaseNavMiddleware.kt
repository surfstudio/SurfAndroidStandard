package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.ExperimentalFeature
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.NavigatorMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator

/**
 * Шаблонный базовый [RxMiddleware] с поддержкой навигации.
 *
 * Middleware с навигацией находится в стадии эксперимента и не рекомендуется к добавлению на реальные проекты.
 */
@ExperimentalFeature
abstract class BaseNavMiddleware<T : Event>(
        baseNavMiddlewareDependency: BaseNavMiddlewareDependency
) : NavigatorMiddleware<T>, BaseMiddleware<T>(
        BaseMiddlewareDependency(
                baseNavMiddlewareDependency.activityNavigator,
                baseNavMiddlewareDependency.schedulersProvider,
                baseNavMiddlewareDependency.errorHandler
        )
) {

    override val dialogNavigator: DialogNavigator = baseNavMiddlewareDependency.dialogNavigator
    override val fragmentNavigator: FragmentNavigator = baseNavMiddlewareDependency.fragmentNavigator

    override val observableRoutes: MutableList<Class<out SupportOnActivityResultRoute<*>>> = mutableListOf()
}