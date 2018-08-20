package ru.surfstudio.android.core.mvp.sample.ui.screen.another

import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_another.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.mvp.sample.R
import ru.surfstudio.android.core.mvp.sample.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

class AnotherActivityView : BaseRenderableActivityView<AnotherScreenModel>() {

    @Inject
    internal lateinit var presenter: AnotherPresenter

    override fun createConfigurator(): ActivityScreenConfigurator = AnotherScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_another

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "AnotherActivityView"

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        finish_btn.setOnClickListener { presenter.finishScreen() }
    }

    override fun renderInternal(screenModel: AnotherScreenModel?) { }
}