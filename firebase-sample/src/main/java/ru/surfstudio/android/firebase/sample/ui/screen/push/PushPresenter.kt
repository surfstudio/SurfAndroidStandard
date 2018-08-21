package ru.surfstudio.android.firebase.sample.ui.screen.push

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class PushPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<PushActivityView>(basePresenterDependency) {

    private val screenModel: PushScreenModel = PushScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}