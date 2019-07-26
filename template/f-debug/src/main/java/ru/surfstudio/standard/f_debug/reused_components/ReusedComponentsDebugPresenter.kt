package ru.surfstudio.standard.f_debug.reused_components

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана для показа переиспользуемых компонентов
 */
@PerScreen
class ReusedComponentsDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<ReusedComponentsDebugActivityView>(basePresenterDependency)
