package ru.surfstudio.standard.f_main

import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_main.fragment1.Fragment1Route
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val fragmentNavigator: FragmentNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    override fun onFirstLoad() {
        super.onFirstLoad()
        fragmentNavigator.add(Fragment1Route(), true, FragmentTransaction.TRANSIT_NONE)
    }
}