package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.NavigatorMiddleware
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvp.binding.rx.builders.UiBuilderFinish
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderIo
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонный базовый [RxMiddleware], который необходимо реализовать на проектах.
 */
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