package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.result_dialog.*
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.mvp.dialog.complex.CoreDialogFragmentView
import ru.surfstudio.android.mvp.dialog.sample.R
import javax.inject.Inject

class ResultDialogView : CoreDialogFragmentView() {

    @Inject lateinit var presenter: ResultDialogPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): BaseFragmentViewConfigurator<*, *> =
            ResultDialogConfigurator(arguments ?: Bundle.EMPTY)

    override fun getScreenName(): String = "ResultDialogView"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.result_dialog, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        result_dialog_finish_button.setOnClickListener { presenter.finishWithResult() }
    }
}