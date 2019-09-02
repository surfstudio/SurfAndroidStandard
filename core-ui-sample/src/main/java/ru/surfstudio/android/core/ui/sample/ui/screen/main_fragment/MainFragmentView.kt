package ru.surfstudio.android.core.ui.sample.ui.screen.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.android.core.ui.sample.R
import javax.inject.Inject

class MainFragmentView : CoreFragmentView(), FragmentContainer {

    @Inject
    lateinit var presenter: MainFragmentPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            MainFragmentConfigurator(arguments ?: Bundle.EMPTY)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.main_fragment, container, false)

    override fun getContentContainerViewId(): Int = R.id.container2

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        button_to_fragment_2_from_fragment_1.setOnClickListener { presenter.openResultFragment() }
    }

    fun showMessage(data: String) {
        message_from_fragment_2.text = data
    }
}