package ru.surfstudio.standard.f_main.fragment2

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import javax.inject.Inject

class Fragment2Presenter @Inject constructor(
    basePresenterDependency: BasePresenterDependency,
    private val route: Fragment2Route,
    private val fragmentNavigator: FragmentNavigator
) : BasePresenter<Fragment2View>(basePresenterDependency) {

    fun closeWithResult() {
        fragmentNavigator.finishWithResult(route, "42", true)
    }
}