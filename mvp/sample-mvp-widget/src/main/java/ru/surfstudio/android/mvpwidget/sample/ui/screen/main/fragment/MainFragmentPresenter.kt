package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.fragment

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import javax.inject.Inject

/**
 * Презентер главного фрагмента
 */
class MainFragmentPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<MainFragmentView>(basePresenterDependency)