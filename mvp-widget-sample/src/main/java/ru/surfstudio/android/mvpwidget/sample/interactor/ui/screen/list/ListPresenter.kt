package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана [ListActivityView]
 */
@PerScreen
class ListPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val route: ListActivityRoute
) : BasePresenter<ListActivityView>(basePresenterDependency) {

    init {
        basePresenterDependency.eventDelegateManager.registerDelegate(OnBackPressedDelegate {
            finishWithHash()
            true
        })
    }

    private val screenModel = ListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun finishWithHash() {
        activityNavigator.finishWithResult(route, view.hashCode().toString())
    }
}
