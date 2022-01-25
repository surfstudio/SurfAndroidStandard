package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_main.*
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.android.mvpwidget.sample.R
import ru.surfstudio.android.mvpwidget.sample.ui.screen.main.MainScreenModel
import javax.inject.Inject

/**
 * Вью главного фрагмента
 */
class MainFragmentView: BaseRenderableFragmentView<MainScreenModel>() {

    @Inject
    lateinit var presenter: MainFragmentPresenter

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator() = MainFragmentScreenConfigurator(arguments ?: bundleOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, viewRecreated: Boolean) {
        constraint_widget_view.render("$this")
    }

    override fun renderInternal(sm: MainScreenModel?) {

    }
}