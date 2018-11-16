package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.frame

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Простой пример презентера для виджета
 */
@PerScreen
class FrameViewPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<FrameWidgetView>(basePresenterDependency)