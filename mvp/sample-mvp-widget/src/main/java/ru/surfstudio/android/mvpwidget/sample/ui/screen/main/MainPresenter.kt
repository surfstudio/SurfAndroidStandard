package ru.surfstudio.android.mvpwidget.sample.ui.screen.main

import android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import android.util.Log
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigatorImpl
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvpwidget.sample.ui.screen.main.list.ListActivityRoute
import ru.surfstudio.android.mvpwidget.sample.ui.screen.main.fragment.MainFragmentRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        activityProvider: ActivityProvider,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val fragmentNavigator = FragmentNavigatorImpl(activityProvider)

    fun openWidgetFragment() {
        fragmentNavigator.add(MainFragmentRoute(), true, TRANSIT_FRAGMENT_FADE)
    }

    fun openListScreen() {
        activityNavigator.start(ListActivityRoute())
    }

    override fun onFirstLoad() {
        Log.d("Activity", "onFirstLoad")
    }

    override fun onLoad(viewRecreated: Boolean) {
        Log.d("Activity", "onLoad")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Activity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Activity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Activity", "onStop")
    }

    override fun onStart() {
        Log.d("Activity", "onStart")
    }

    override fun onViewDetach() {
        super.onViewDetach()
        Log.d("Activity", "onDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Activity", "onDestroy")
    }

}