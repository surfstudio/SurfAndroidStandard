package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.relative

import android.util.Log
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import javax.inject.Inject

/**
 * Простой пример презентера для виджета
 */
@PerScreen
class RelativeViewPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<RelativeWidgetView>(basePresenterDependency) {

    override fun onFirstLoad() {
        Log.d("Widget", "onFirstLoad")
    }

    override fun onLoad(viewRecreated: Boolean) {
        Log.d("Widget", "onLoad")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Widget", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Widget", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Widget", "onStop")
    }

    override fun onStart() {
        Log.d("Widget", "onStart")
    }

    override fun onViewDetach() {
        super.onViewDetach()
        Log.d("Widget", "onDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Widget", "onDestroy")
    }
}