package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.constraint

import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Простой пример презентера для виджета
 */
@PerScreen
class ConstraintViewPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<ConstraintWidgetView>(basePresenterDependency) {

    private var changeTextDisposable = Disposables.disposed()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        Logger.i("1111 Widget onLoad ${hashCode()}")
    }

    override fun onFirstLoad() {
        Logger.d("1111 Widget onFirstLoad ${hashCode()}")
        super.onFirstLoad()
    }

    override fun onStart() {
        super.onStart()
        Logger.i("1111 Widget onStart ${hashCode()}")
    }

    override fun onResume() {
        super.onResume()
        Logger.i("1111 Widget onResume ${hashCode()}")
    }

    override fun onPause() {
        super.onPause()
        Logger.i("1111 Widget onPause ${hashCode()}")
    }

    override fun onStop() {
        super.onStop()
        Logger.i("1111 Widget onStop ${hashCode()}")
    }

    override fun onViewDetach() {
        super.onViewDetach()
        Logger.i("1111 Widget onViewDetach ${hashCode()}")
    }

    override fun onDestroy() {
        Logger.i("1111 Widget onDestroy ${hashCode()}")
        super.onDestroy()
    }

    fun changeTextOnWidget() {
        changeTextDisposable.dispose()
        changeTextDisposable = subscribe(Observable.timer(500L, TimeUnit.MILLISECONDS), {
            view.render("presenter")
            Logger.i("11111 Widget ${view.hashCode()} receive event")
        }, {

        })
    }
}