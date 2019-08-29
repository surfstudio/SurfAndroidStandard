package ru.surfstudio.android.core.ui.sample.ui.screen.fragment_2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_2.*
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.sample.R
import javax.inject.Inject

class Fragment2View : CoreFragmentView() {

    @Inject
    lateinit var presenter: Fragment2Presenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            Fragment2Configurator(arguments ?: Bundle.EMPTY)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_2, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        button_back_from_fragment_2.setOnClickListener { presenter.backToFragment1() }
    }
}