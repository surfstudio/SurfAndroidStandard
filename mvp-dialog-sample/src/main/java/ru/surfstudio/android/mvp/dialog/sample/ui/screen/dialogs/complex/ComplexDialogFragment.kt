package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.complex_dialog_layout.*
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.mvp.dialog.complex.CoreDialogFragmentView
import ru.surfstudio.android.mvp.dialog.sample.R
import javax.inject.Inject

class ComplexDialogFragment : CoreDialogFragmentView() {

    @Inject
    internal lateinit var presenter: ComplexDialogPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> {
        return ComplexDialogScreenConfigurator(arguments)
    }

    override fun getScreenName(): String = "Complex Dialog Fragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.complex_dialog_layout, container)
        return view
    }

    fun render(screenModel: ComplexDialogScreenModel) {
        value_tv.text = screenModel.sampleData.toString()
    }
}