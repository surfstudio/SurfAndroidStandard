package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvp.binding.rx.builders.UiBuilderFinish
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.builders.FlatMapMiddleware
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderIo
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонный базовый [RxMiddleware], который необходимо реализовать на проектах.
 */
abstract class BaseMapMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMiddleware<T>(baseMiddlewareDependency), FlatMapMiddleware<T>