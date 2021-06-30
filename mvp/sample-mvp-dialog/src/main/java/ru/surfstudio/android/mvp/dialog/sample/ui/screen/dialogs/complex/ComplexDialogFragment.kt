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
        return ComplexDialogScreenConfigurator(arguments!!)
    }

    override fun getScreenName(): String = "Complex Dialog Fragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.complex_dialog_layout, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, viewRecreated: Boolean) {
        increase_btn.setOnClickListener { presenter.increment() }
        decrease_btn.setOnClickListener { presenter.decrement() }
        apply_btn.setOnClickListener { presenter.applyChanges() }
    }

    fun render(sm: ComplexDialogScreenModel) {
        value_tv.text = sm.sampleData.toString()
    }
}