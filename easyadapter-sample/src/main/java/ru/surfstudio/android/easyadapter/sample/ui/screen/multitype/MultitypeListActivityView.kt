package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.sample.R
import javax.inject.Inject

class MultitypeListActivityView : BaseRenderableActivityView<MultitypeListScreenModel>() {

    @Inject
    lateinit var presenter: MultitypeListPresenter

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> {
        return MultitypeListScreenConfigurator(intent)
    }

    override fun getContentView(): Int = R.layout.multitype_list_layout

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "Multitype List Activity"

    override fun renderInternal(screenModel: MultitypeListScreenModel?) {

    }
}