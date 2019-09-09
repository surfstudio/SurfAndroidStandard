package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import ru.surfstudio.android.mvp.dialog.sample.R
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment
import javax.inject.Inject

class SimpleDialogFragment : CoreSimpleDialogFragment() {

    @Inject lateinit var presenter: SimpleDialogPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getScreenComponent(SimpleDialogComponent::class.java).inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
                .setTitle(R.string.simple_dialog_title)
                .setMessage(R.string.simple_dialog_message)
                .setPositiveButton(R.string.positive_btn_text) { _, _ ->  presenter.simpleDialogSuccessAction() }
                .setNegativeButton(R.string.negative_btn_text, null)
                .create()
    }

    override fun getName(): String = "Simple Dialog Fragment"
}