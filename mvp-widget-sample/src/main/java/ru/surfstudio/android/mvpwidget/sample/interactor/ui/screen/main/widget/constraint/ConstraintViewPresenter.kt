package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.constraint

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list.ListActivityRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Просто пример презентера для виджета
 * */
@PerScreen
class ConstraintViewPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<ConstraintWidgetView>(basePresenterDependency) {

    private var changeTextDisposable = Disposables.disposed()

    override fun onFirstLoad() {
        super.onFirstLoad()
        subscribe(activityNavigator.observeResult(ListActivityRoute::class.java), {
            Logger.i("111111 Widget observeResult $it")
        })
    }

    fun changeTextOnWidget() {
        changeTextDisposable.dispose()
        changeTextDisposable = subscribe(Observable.timer(500L, TimeUnit.MILLISECONDS), {
            view.render("change text")
            Logger.i("11111 Widget ${view.hashCode()} receive event")

            activityNavigator.startForResult(ListActivityRoute())
        }, {

        })
    }
}