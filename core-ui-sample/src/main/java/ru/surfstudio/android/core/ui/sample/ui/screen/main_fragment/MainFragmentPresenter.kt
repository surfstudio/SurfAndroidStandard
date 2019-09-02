package ru.surfstudio.android.core.ui.sample.ui.screen.main_fragment

import android.app.FragmentTransaction
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.sample.ui.screen.result_fragment.ResultFragmentRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class MainFragmentPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val route: MainFragmentRoute,
        private val fragmentNavigator: FragmentNavigator
) : BasePresenter<MainFragmentView>(
        basePresenterDependency
) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        subscribe(fragmentNavigator.observeResult<String>(ResultFragmentRoute::class.java)) {
            if (it.isSuccess) {
                view.showMessage(it.data)
            }
        }
    }

    fun openResultFragment() {
        fragmentNavigator.replaceFragmentForResult(route, ResultFragmentRoute(), FragmentTransaction.TRANSIT_NONE)
    }
}