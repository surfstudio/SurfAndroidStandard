package ru.surfstudio.standard.f_main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.android.template.f_main.R
import ru.surfstudio.standard.f_main.di.MainScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRxActivityView(), FragmentContainer {

    @Inject
    lateinit var bm: MainBindModel

    override fun createConfigurator() = MainScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun getContentContainerViewId() = R.id.main_fragment_container

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        initViews()
        initListeners()
    }

    override fun getScreenName(): String = "MainActivityView"

    private fun initViews() {
        bm.tabTypeState.bindTo(main_bottom_bar::updateSelection)
    }

    private fun initListeners() {
        main_bottom_bar.tabSelectedAction = { bm.tabSelectedAction.accept(it) }
    }
}
