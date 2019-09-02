package ru.surfstudio.android.core.ui.sample.ui.screen.result_fragment

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

const val FRAGMENT_2_RESULT = "FRAGMENT_2_RESULT"

@PerScreen
class SecondFragmentPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val route: ResultFragmentRoute,
        private val fragmentNavigator: FragmentNavigator
) : BasePresenter<ResultFragmentView>(
        basePresenterDependency
) {
    fun backToFragment1() {
        fragmentNavigator.finishWithResult(route, FRAGMENT_2_RESULT)
    }
}