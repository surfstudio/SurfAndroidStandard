package ru.surfstudio.android.core.mvp.sample.ui.screen.another

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.mvp.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

class AnotherActivityView : BaseRenderableActivityView<AnotherScreenModel>() {

    @Inject
    internal lateinit var presenter: AnotherPresenter

    override fun createConfigurator(): DefaultActivityScreenConfigurator = AnotherScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_another

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "AnotherActivityView"

    override fun renderInternal(sm: AnotherScreenModel?) { }
}