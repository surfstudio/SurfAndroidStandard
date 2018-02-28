package ru.surfstudio.standard.ui.screen.tabs

import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_tabs.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.standard.R
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
class TabsActivityView : BaseRenderableActivityView<TabsActivityScreenModel>(), FragmentContainer {
    override fun getContentContainerViewId(): Int  = R.id.container

    @Inject
    lateinit var presenter: TabsActivityPresenter

    override fun renderInternal(screenModel: TabsActivityScreenModel?) {

    }

    override fun getPresenters() = arrayOf(presenter)

    override fun getName(): String = TabsActivityView::class.toString()

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> =
            TabsActvityScreenConfigurator(intent)

    override fun getContentView(): Int  = R.layout.activity_tabs

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        first_fr_btn.setOnClickListener { presenter.openTab(1) }
        scnd_fr_btn.setOnClickListener { presenter.openTab(2) }
        third_fr_btn.setOnClickListener { presenter.openTab(3) }
        fourth_fr_btn.setOnClickListener { presenter.openTab(4) }
    }
}