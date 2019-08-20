package ru.surfstudio.standard.f_main.fragment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_2.*
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.android.template.f_main.R
import javax.inject.Inject

class Fragment2View : BaseRenderableFragmentView<Fragment2ScreenModel>() {

    @Inject
    lateinit var presenter: Fragment2Presenter

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator() = Fragment2Configurator(arguments
        ?: bundleOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_2, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        button_back.setOnClickListener { presenter.closeWithResult() }
    }

    override fun renderInternal(sm: Fragment2ScreenModel) {

    }
}