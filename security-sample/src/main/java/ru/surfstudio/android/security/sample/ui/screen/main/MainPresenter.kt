package ru.surfstudio.android.security.sample.ui.screen.main

import androidx.core.widget.toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.security.root.RootChecker
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    fun checkRoot() {
        if (RootChecker.isRoot) {
            view.toast("Root detected!")
        } else {
            view.toast("No Root")
        }
    }
}
