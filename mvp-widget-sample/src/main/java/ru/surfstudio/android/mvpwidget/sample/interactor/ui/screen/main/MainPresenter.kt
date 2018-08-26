package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<MainActivityView>(basePresenterDependency)