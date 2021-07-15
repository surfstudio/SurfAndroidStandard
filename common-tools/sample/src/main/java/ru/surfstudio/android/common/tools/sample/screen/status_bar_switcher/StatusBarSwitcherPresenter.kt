package ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher

import ru.surfstudio.android.common.tools.statusbarswitcher.StatusBarSwitcher
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class StatusBarSwitcherPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val statusBarSwitcher: StatusBarSwitcher
) : BasePresenter<StatusBarSwitcherActivityView>(basePresenterDependency) {

    private val screenModel = StatusBarSwitcherScreenModel()

    override fun onFirstLoad() {
        view.render(screenModel)
    }

    override fun onResume() {
        super.onResume()
        if (screenModel.statusBarHeight != 0 && screenModel.isAutoSwitchingEnabled) {
            attachStatusBarSwitcher()
        }
    }

    override fun onPause() {
        super.onPause()
        statusBarSwitcher.detach()
    }

    fun setStatusBarHeight(statusBarHeight: Int) {
        val shouldEnableStatusBarSwitcher = screenModel.statusBarHeight == 0 && screenModel.isAutoSwitchingEnabled
        screenModel.statusBarHeight = statusBarHeight
        if (shouldEnableStatusBarSwitcher) {
            attachStatusBarSwitcher()
        }
    }

    fun toggleStatusBarSwitchingMode() {
        val newIsAutoSwitchingEnabled = !screenModel.isAutoSwitchingEnabled
        screenModel.isAutoSwitchingEnabled = newIsAutoSwitchingEnabled

        if (newIsAutoSwitchingEnabled) {
            attachStatusBarSwitcher()
        } else {
            statusBarSwitcher.detach()
        }

        view.render(screenModel)
    }

    fun closeScreen() {
        finish()
    }

    private fun attachStatusBarSwitcher() {
        statusBarSwitcher.setStatusBarHeight(screenModel.statusBarHeight)
        view.attachStatusBarSwitcher(statusBarSwitcher)
    }
}