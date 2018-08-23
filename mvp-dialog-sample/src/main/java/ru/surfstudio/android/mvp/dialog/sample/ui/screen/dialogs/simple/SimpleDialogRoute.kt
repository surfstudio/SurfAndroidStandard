package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple

import android.support.v4.app.DialogFragment
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute

class SimpleDialogRoute : DialogRoute() {
    override fun getFragmentClass(): Class<out DialogFragment> = SimpleDialogFragment::class.java
}