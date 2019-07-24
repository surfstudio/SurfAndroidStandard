package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.constraint

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
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
        Log.d("1111 Widget", "onLoad w=${view.hashCode()}p=${hashCode()}")
    }

    override fun onFirstLoad() {
        Log.d("1111 Widget", " onFirstLoad w=${view.hashCode()}p=${hashCode()}")
        super.onFirstLoad()
    }

    override fun onStart() {
        super.onStart()
        Log.d("1111 Widget", " onStart w=${view.hashCode()}p=${hashCode()}")
    }

    override fun onResume() {
        super.onResume()
        Log.d("1111 Widget", " onResume w=${view.hashCode()}p=${hashCode()}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("1111 Widget", " onPause w=${view.hashCode()}p=${hashCode()}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("1111 Widget", " onStop w=${view.hashCode()}p=${hashCode()}")
    }

    override fun onViewDetach() {
        super.onViewDetach()
        Log.d("1111 Widget", " onViewDetach w=${view.hashCode()}p=${hashCode()}")
    }

    override fun onDestroy() {
        Log.d("1111 Widget", " onDestroy w=${null}p=${hashCode()}")
        super.onDestroy()
    }

    fun changeTextOnWidget() {
        changeTextDisposable.dispose()
        changeTextDisposable = subscribe(Observable.timer(500L, TimeUnit.MILLISECONDS), {
            view.render("presenter")
            Log.d("11111 Widget", "w=${view.hashCode()}p=${hashCode()} receive event")
        }, {

        })
    }
}