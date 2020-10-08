package ru.surfstudio.android.navigation.sample_standard.screen.dialogs.amazing

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.BaseSimpleDialogRoute
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.BaseSimpleDialogView
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.SimpleDialogResult

class AmazingDialog: BaseSimpleDialogView() {

    override val route: BaseSimpleDialogRoute = AmazingDialogRoute()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setTitle("Amazing title")
                .setMessage("Gorgeous message")
                .setPositiveButton("Ok") { _, _ -> closeWithResult(SimpleDialogResult.DISMISS) }
                .create()
    }
}