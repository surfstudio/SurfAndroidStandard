package ru.surfstudio.standard.f_main.fragment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_1.*
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.android.template.f_main.R
import javax.inject.Inject

class Fragment1View : BaseRenderableFragmentView<Fragment1ScreenModel>() {

    @Inject
    lateinit var presenter: Fragment1Presenter

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator() = Fragment1Configurator(arguments
        ?: bundleOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_1, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        button_add.setOnClickListener { presenter.addFragment2() }
        button_replace.setOnClickListener { presenter.replaceFragment2() }
    }

    override fun renderInternal(sm: Fragment1ScreenModel) {

    }
}