package ru.surfstudio.android.mvpwidget.sample.ui.screen.main

import android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import android.app.FragmentTransaction.TRANSIT_NONE
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list.ListActivityRoute
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

    private val fragmentNavigator = FragmentNavigator(activityProvider)

    fun openWidgetFragment() {
        fragmentNavigator.add(MainFragmentRoute(), true, TRANSIT_FRAGMENT_FADE)
    }

    fun openListScreen() {
        activityNavigator.start(ListActivityRoute())
    }
}