package ru.surfstudio.standard.f_search

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class SearchPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BaseRxPresenter(basePresenterDependency)
