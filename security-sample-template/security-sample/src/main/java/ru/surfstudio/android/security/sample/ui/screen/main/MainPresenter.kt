package ru.surfstudio.android.security.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.StringsProvider
import ru.surfstudio.android.security.root.RootChecker
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.domain.ApiKey
import ru.surfstudio.android.security.sample.ui.screen.pin.CreatePinActivityRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                        private val activityNavigator: ActivityNavigator,
                                        private val stringsProvider: StringsProvider
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun checkRoot() {
        val message = stringsProvider.getString(
                if (RootChecker.isRoot)
                    R.string.root_message
                else
                    R.string.no_root_message)
        view.showMessage(message)
    }

    fun createPin(apiKey: String) {
        if (apiKey.isNotEmpty()) {
            activityNavigator.start(CreatePinActivityRoute(ApiKey(apiKey)))
        }
    }
}
