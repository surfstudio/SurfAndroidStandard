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
        Log.d("WidgetLifecycle", "onLoad w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
        subscribe(Observable.timer(5, TimeUnit.SECONDS)) { Log.d("isViewAttached?", "${view?.widgetDataId} ${view?.hashCode()}") }
    }

    override fun onFirstLoad() {
        Log.d("WidgetLifecycle", " onFirstLoad w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
        super.onFirstLoad()
    }

    override fun onStart() {
        super.onStart()
        Log.d("WidgetLifecycle", " onStart w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
    }

    override fun onResume() {
        super.onResume()
        Log.d("WidgetLifecycle", " onResume w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("WidgetLifecycle", " onPause w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("WidgetLifecycle", " onStop w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
    }

    override fun onViewDetach() {
        super.onViewDetach()
        Log.d("WidgetLifecycle", " onViewDetach w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
    }

    override fun onDestroy() {
        Log.d("WidgetLifecycle", " onDestroy w=${null}vh=${null} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()}")
        super.onDestroy()
    }

    fun changeTextOnWidget() {
        changeTextDisposable.dispose()
        changeTextDisposable = subscribe(Observable.timer(500L, TimeUnit.MILLISECONDS),
                {
                    view.render("presenter")
                    Log.d("1WidgetLifecycle", "w=${view.widgetDataId}vh=${view.hashCode()} p=${hashCode()} wd=${view?.widgetViewDelegate?.hashCode()} receive event")
                }, {

        })
    }
}