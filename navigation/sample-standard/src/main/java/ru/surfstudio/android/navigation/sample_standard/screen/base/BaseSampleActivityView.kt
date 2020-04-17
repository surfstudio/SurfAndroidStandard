package ru.surfstudio.android.navigation.sample_standard.screen.base

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.navigation.provider.id.IdentifiableScreen
import ru.surfstudio.android.navigation.route.Route

abstract class BaseSampleActivityView : BaseRxActivityView(), IdentifiableScreen {

    override val screenId: String = intent?.getStringExtra(Route.SCREEN_ID) ?: ""

    override fun getScreenName(): String = screenId
}