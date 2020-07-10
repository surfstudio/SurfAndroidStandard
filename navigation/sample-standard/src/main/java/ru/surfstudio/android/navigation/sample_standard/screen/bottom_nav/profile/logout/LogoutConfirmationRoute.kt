package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.profile.logout

import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.BaseSimpleDialogRoute

class LogoutConfirmationRoute : BaseSimpleDialogRoute() {

    override fun getScreenClass(): Class<out DialogFragment>? {
        return LogoutConfirmationDialog::class.java
    }
}