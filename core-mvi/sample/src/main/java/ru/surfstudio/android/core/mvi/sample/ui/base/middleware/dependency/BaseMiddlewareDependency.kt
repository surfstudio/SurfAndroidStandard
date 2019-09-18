package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency

import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонная зависимость для [BaseNavMiddleware], которую реализовать на проектах.
 */
class BaseMiddlewareDependency(
        val activityNavigator: ActivityNavigator,
        val schedulersProvider: SchedulersProvider,
        val errorHandler: ErrorHandler
)