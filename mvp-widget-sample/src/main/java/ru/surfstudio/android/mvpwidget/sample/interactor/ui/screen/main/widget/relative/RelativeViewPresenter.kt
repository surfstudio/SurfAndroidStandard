package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.relative

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Просто пример презентера для виджета
 * */
@PerScreen
class RelativeViewPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<RelativeWidgetView>(basePresenterDependency)