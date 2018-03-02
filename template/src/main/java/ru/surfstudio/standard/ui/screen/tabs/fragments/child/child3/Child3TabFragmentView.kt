package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_child_tab.*
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.standard.R
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
class Child3TabFragmentView : BaseRenderableFragmentView<Child3TabFragmentScreenModel>() {

    @Inject
    lateinit var presenter: Child3TabFragmentPresenter

    override fun renderInternal(screenModel: Child3TabFragmentScreenModel) {
        tv_child.text = "child : ${screenModel.id}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_child_tab, container, false)
    }

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            Child3TabFragmentConfigurator(arguments!!)

    override fun getScreenName(): String = this::class.toString()
}