package ru.surfstudio.standard.f_main.fragment1

import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.f_main.fragment2.Fragment2Route
import javax.inject.Inject

class Fragment1Presenter @Inject constructor(
    basePresenterDependency: BasePresenterDependency,
    private val route: Fragment1Route,
    private val activityNavigator: ActivityNavigator,
    private val fragmentNavigator: FragmentNavigator
) : BasePresenter<Fragment1View>(basePresenterDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        subscribe(fragmentNavigator.observeResult(Fragment2Route::class.java)) {
            Logger.d(it.data)
        }
        subscribe(activityNavigator.observeResult(Fragment2Route::class.java)) {
            Logger.d(it.data)
        }
    }

    fun addFragment2() {
        fragmentNavigator.addFragmentForResult(route, Fragment2Route(), true, FragmentTransaction.TRANSIT_NONE)
    }

    fun replaceFragment2() {
        fragmentNavigator.replaceFragmentForResult(route, Fragment2Route(), true, FragmentTransaction.TRANSIT_NONE)
    }
}