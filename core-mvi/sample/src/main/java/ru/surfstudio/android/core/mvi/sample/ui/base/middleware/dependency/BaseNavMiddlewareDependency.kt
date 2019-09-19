package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency

import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонная зависимость для [BaseNavMiddleware], которую нужно провайдить на проектах.
 */
class BaseNavMiddlewareDependency(
        val activityNavigator: ActivityNavigator,
        val fragmentNavigator: FragmentNavigator,
        val dialogNavigator: DialogNavigator,
        val schedulersProvider: SchedulersProvider,
        val errorHandler: ErrorHandler
)