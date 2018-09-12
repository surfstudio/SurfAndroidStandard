package ru.surfstudio.android.security.sample.ui.screen.session

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана сессии
 */
@PerScreen
class SessionPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<SessionActivityView>(basePresenterDependency)
