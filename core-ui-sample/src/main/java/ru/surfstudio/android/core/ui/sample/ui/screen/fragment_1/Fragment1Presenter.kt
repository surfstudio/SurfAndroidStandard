package ru.surfstudio.android.core.ui.sample.ui.screen.fragment_1

import android.app.FragmentTransaction
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.sample.ui.screen.fragment_2.Fragment2Route
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class Fragment1Presenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val route: Fragment1Route,
        private val fragmentNavigator: FragmentNavigator
) : BasePresenter<Fragment1View>(
        basePresenterDependency
) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        subscribe(fragmentNavigator.observeResult<String>(Fragment2Route::class.java)) {
            if (it.isSuccess) {
                view.showMessage(it.data)
            }
        }
    }

    fun openFragment2() {
        fragmentNavigator.replaceFragmentForResult(route, Fragment2Route(), FragmentTransaction.TRANSIT_NONE)
    }
}