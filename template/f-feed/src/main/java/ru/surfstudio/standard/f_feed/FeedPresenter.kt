package ru.surfstudio.standard.f_feed

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class FeedPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
): BaseRxPresenter(basePresenterDependency)