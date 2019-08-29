package ru.surfstudio.android.core.ui.sample.ui.screen.main

import android.app.FragmentTransaction
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.sample.ui.screen.fragment_1.Fragment1Route
import ru.surfstudio.android.core.ui.sample.ui.screen.fragment_2.Fragment2Route
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val fragmentNavigator: FragmentNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    override fun onFirstLoad() {
        subscribe(fragmentNavigator.observeResult(Fragment2Route::class.java)) {
            if (it.isSuccess) {
                view.showMessage(it.data)
            }
        }
        fragmentNavigator.add(Fragment1Route(), false, FragmentTransaction.TRANSIT_NONE)
    }

    fun openFragment2() {
        fragmentNavigator.replaceFragmentForResultFromActivity(Fragment2Route(), FragmentTransaction.TRANSIT_NONE)
    }
}