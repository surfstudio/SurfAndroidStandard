package ru.surfstudio.android.core.ui.sample.ui.screen.fragment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_1.*
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.android.core.ui.sample.R
import javax.inject.Inject

class Fragment1View : CoreFragmentView(), FragmentContainer {

    @Inject
    lateinit var presenter: Fragment1Presenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            Fragment1Configurator(arguments ?: Bundle.EMPTY)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_1, container, false)

    override fun getContentContainerViewId(): Int = R.id.container2

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        button_to_fragment_2_from_fragment_1.setOnClickListener { presenter.openFragment2() }
    }

    fun showMessage(data: String) {
        message_from_fragment_2.text = data
    }
}