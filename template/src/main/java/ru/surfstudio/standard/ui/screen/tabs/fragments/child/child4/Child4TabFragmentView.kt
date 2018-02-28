package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child4

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
class Child4TabFragmentView : BaseRenderableFragmentView<Child4TabFragmentScreenModel>() {

    @Inject
    lateinit var presenter: Child4TabFragmentPresenter

    override fun renderInternal(screenModel: Child4TabFragmentScreenModel) {
        tv_child.text = "child : ${screenModel.id}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_child_tab, container, false)
    }

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            Child4TabFragmentConfigurator(arguments!!)

    override fun getName(): String = this::class.toString()
}