package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child1

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
class ChildTabFragmentView : BaseRenderableFragmentView<ChildTabFragmentScreenModel>() {

    @Inject
    lateinit var presenter: ChildTabFragmentPresenter

    override fun renderInternal(screenModel: ChildTabFragmentScreenModel) {
        tv_child.text = "child : ${screenModel.id}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_child_tab, container, false)
    }

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            ChildTabFragmentConfigurator(arguments!!)

    override fun getName(): String = this::class.toString()
}