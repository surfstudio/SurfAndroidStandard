package ru.surfstudio.standard.f_feed

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class FeedPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
): BasePresenter<FeedFragmentView>(basePresenterDependency) {

}