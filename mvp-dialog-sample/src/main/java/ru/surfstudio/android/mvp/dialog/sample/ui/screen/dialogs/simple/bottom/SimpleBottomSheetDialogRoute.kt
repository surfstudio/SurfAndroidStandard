package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.simple.bottom

import android.support.v4.app.DialogFragment
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute

class SimpleBottomSheetDialogRoute : DialogRoute() {
    override fun getFragmentClass(): Class<out DialogFragment> = SimpleBottomSheetDialogFragment::class.java
}