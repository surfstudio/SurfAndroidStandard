package ru.surfstudio.standard.f_debug.common_controllers

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана для показа переиспользуемых контроллеров
 */
@PerScreen
class CommonControllersDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<CommonControllersDebugActivityView>(basePresenterDependency)
