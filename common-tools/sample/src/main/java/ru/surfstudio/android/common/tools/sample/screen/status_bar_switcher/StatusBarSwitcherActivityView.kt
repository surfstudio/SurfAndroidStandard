package ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import kotlinx.android.synthetic.main.activity_status_bar_switcher.*
import ru.surfstudio.android.common.tools.sample.R
import ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher.controller.ImageItemController
import ru.surfstudio.android.common.tools.statusbarswitcher.StatusBarSwitcher
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.utilktx.ktx.ui.view.setTopMargin
import javax.inject.Inject

internal class StatusBarSwitcherActivityView : BaseRenderableActivityView<StatusBarSwitcherScreenModel>() {

    @Inject
    lateinit var presenter: StatusBarSwitcherPresenter

    private val easyAdapter = EasyAdapter()
    private val itemController = ImageItemController()

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> =
            StatusBarSwitcherScreenConfigurator(intent)

    override fun getScreenName() = "StatusBarSwitcherActivityView"

    override fun getContentView() = R.layout.activity_status_bar_switcher

    override fun getPresenters() = arrayOf(presenter)

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            status_bar_switcher_back_btn.setTopMargin(status_bar_switcher_back_btn.marginTop + insets.stableInsetTop)
            presenter.setStatusBarHeight(insets.stableInsetTop)
            insets
        }
        setFullscreenMode()
        initViews()
    }

    fun attachStatusBarSwitcher(statusBarSwitcher: StatusBarSwitcher) {
        statusBarSwitcher.attach(this)
    }

    private fun initViews() {
        status_bar_switcher_rv.adapter = easyAdapter

        status_bar_switcher_back_btn.setOnClickListener { presenter.closeScreen() }
        status_bar_switcher_mode_btn.setOnClickListener { presenter.toggleStatusBarSwitchingMode() }
    }

    override fun renderInternal(sm: StatusBarSwitcherScreenModel) {
        val switchModeTextRes = when {
            sm.isAutoSwitchingEnabled -> R.string.status_bar_auto_switch_mode_btn_enabled_text
            else -> R.string.status_bar_auto_switch_mode_btn_disabled_text
        }
        status_bar_switcher_mode_btn.text = getString(switchModeTextRes)

        val itemList = ItemList().addAll(sm.sampleList, itemController)
        easyAdapter.setItems(itemList)
    }

    private fun setFullscreenMode() {
        val decorView = window?.decorView ?: return
        val systemUiVisibilityFlags = decorView.systemUiVisibility
        decorView.systemUiVisibility = systemUiVisibilityFlags or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}
