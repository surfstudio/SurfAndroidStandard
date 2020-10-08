package ru.surfstudio.android.navigation.sample_standard.screen.dialogs.amazing

import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.BaseSimpleDialogRoute

class AmazingDialogRoute : BaseSimpleDialogRoute() {

    override fun getScreenClass(): Class<out DialogFragment>? {
        return AmazingDialog::class.java
    }

}