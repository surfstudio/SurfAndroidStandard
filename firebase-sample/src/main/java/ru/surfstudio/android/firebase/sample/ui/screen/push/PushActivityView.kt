package ru.surfstudio.android.firebase.sample.ui.screen.push

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.firebase.sample.R
import ru.surfstudio.android.firebase.sample.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

/**
 * Экран, который будет открыт для пушей без данных
 */
class PushActivityView : BaseRenderableActivityView<PushScreenModel>() {

    @Inject
    internal lateinit var presenter: PushPresenter

    override fun createConfigurator(): ActivityScreenConfigurator = PushScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_push

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "PushActivityView"

    override fun renderInternal(screenModel: PushScreenModel?) { }
}