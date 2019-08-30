package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.list

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
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

    private val screenModel = ListScreenModel()

    init {
        basePresenterDependency.eventDelegateManager.registerDelegate(OnBackPressedDelegate {
            finishWithHash()
            true
        })
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
        subscribe(Observable.timer(2500, TimeUnit.MILLISECONDS), ::fillWithNewElements) // заполняем список новыми элементами через 2.5с
    }

    private fun fillWithNewElements(ignore: Long) {
        screenModel.itemList = screenModel.createNewItemList()
        view.render(screenModel)
    }


    fun finishWithHash() {
        activityNavigator.finishWithResult(route, view.hashCode().toString())
    }

}
