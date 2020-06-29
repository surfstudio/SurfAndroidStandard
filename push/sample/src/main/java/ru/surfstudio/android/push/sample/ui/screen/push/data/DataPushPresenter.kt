package ru.surfstudio.android.push.sample.ui.screen.push.data

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class DataPushPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                     route: DataPushActivityRoute
) : BasePresenter<DataPushActivityView>(basePresenterDependency) {

    private val sm = DataPushScreenModel(route.notification)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }
}