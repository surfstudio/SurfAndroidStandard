package ru.surfstudio.standard.ui.screen.tabs.fragments.tab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tab.*
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.standard.R
import javax.inject.Inject

/**
 * Created by azaytsev on 27.02.18.
 */
class Tab3FragmentView : BaseRenderableFragmentView<Tab3FragmentScreenModel>() {

    @Inject
    lateinit var presenter: Tab3FragmentPresenter

    override fun renderInternal(screenModel: Tab3FragmentScreenModel) {
        tv.text = "parent : ${screenModel.id}"
        //activity?.toast()
    }

    override fun getPresenters() = arrayOf(presenter)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false).apply {
            setOnClickListener { presenter.openTab() }
        }
    }

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            Tab3FragmentConfigurator(arguments!!)

    override fun getScreenName(): String = this::class.toString()
}