package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.profile.logout

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.BaseSimpleDialogRoute
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.BaseSimpleDialogView
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.SimpleDialogResult

class LogoutConfirmationDialog : BaseSimpleDialogView() {

    override val route: BaseSimpleDialogRoute = LogoutConfirmationRoute()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setTitle("Are you sure want to log out?")
                .setPositiveButton("Yes") { _, _ -> closeWithResult(SimpleDialogResult.POSITIVE) }
                .setNegativeButton("No") { _, _ -> closeWithResult(SimpleDialogResult.NEGATIVE) }
                .create()
    }
}